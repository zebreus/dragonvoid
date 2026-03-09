{ pkgs ? import <nixpkgs> { }
}:

let
  jdk = pkgs.jdk17;
  jre = pkgs.jre_minimal.override {
    jdk = pkgs.jdk17;
    modules = [ "java.base" "java.desktop" "java.logging" ];
  };
  gradle = pkgs.gradle;

  # Pre-fetch Maven dependencies for reproducible offline builds
  gson = pkgs.fetchurl {
    url = "https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.9/gson-2.8.9.jar";
    hash = "sha256-05mSkYVd5JXJTHQ3YbirUXbP6r4oGlqw2OjUUyb9cD4=";
  };
  json-java = pkgs.fetchurl {
    url = "https://repo1.maven.org/maven2/org/json/json/20211205/json-20211205.jar";
    hash = "sha256-fzjWH7t+Kv3DHGvoZXIO5PyKDDwU+sTz7Ef9Pes5OcY=";
  };

  src = pkgs.lib.cleanSource ./.;

  # Build the Java classes (no world data) so the Editor tool can be run
  dragonvoid-jar-code = pkgs.stdenv.mkDerivation {
    pname = "dragonvoid-jar-code";
    version = "0.1.0";
    inherit src;

    nativeBuildInputs = [ gradle jdk ];

    buildPhase = ''
      export GRADLE_USER_HOME=$(mktemp -d)
      export HOME=$(mktemp -d)

      # Set up a local flat-dir repository with pre-fetched dependencies
      mkdir -p libs
      cp ${gson} libs/gson-2.8.9.jar
      cp ${json-java} libs/json-20211205.jar

      # Compile only – skip world-generation tasks entirely
      gradle --no-daemon --offline compileJava
    '';

    installPhase = ''
      mkdir -p $out/classes $out/libs
      cp -r build/classes/java/main/. $out/classes/
      cp ${gson} $out/libs/gson-2.8.9.jar
      cp ${json-java} $out/libs/json-20211205.jar
    '';
  };

  # Generate a single world from a TMX source file using tiled and the Editor
  mkWorld = worldName: tmxFile: pkgs.stdenv.mkDerivation {
    pname = "dragonvoid-world-${worldName}";
    version = "0.1.0";
    inherit src;

    nativeBuildInputs = [ pkgs.tiled jdk ];

    buildPhase = ''
      # Export the TMX map to JSON using the tiled CLI (format auto-detected from .json extension)
      # QT_QPA_PLATFORM=offscreen is required so tiled can run in a headless build environment
      QT_QPA_PLATFORM=offscreen tiled --export-map res/${tmxFile} world-export.json

      # Convert the JSON to game world data using the Editor
      java -cp "${dragonvoid-jar-code}/classes:${dragonvoid-jar-code}/libs/gson-2.8.9.jar:${dragonvoid-jar-code}/libs/json-20211205.jar" \
        tbs.editor.Editor world-export.json "$out"
    '';

    installPhase = "# World data is written directly to \$out during buildPhase";
  };

  dragonvoid-world-arena = mkWorld "arena" "arena.tmx";
  dragonvoid-world-smalltest = mkWorld "smalltest" "smallTest.tmx";
  dragonvoid-world-lennartswelt = mkWorld "lennartswelt" "testproject.tmx";

  dragonvoid-jar = pkgs.stdenv.mkDerivation {
    pname = "dragonvoid-jar";
    version = "0.1.0";
    inherit src;

    nativeBuildInputs = [ gradle jdk ];

    buildPhase = ''
      export GRADLE_USER_HOME=$(mktemp -d)
      export HOME=$(mktemp -d)

      # Set up a local flat-dir repository with pre-fetched dependencies
      mkdir -p libs
      cp ${gson} libs/gson-2.8.9.jar
      cp ${json-java} libs/json-20211205.jar

      # Copy the pre-built world data into the source tree so Gradle picks it up.
      # The mkWorld derivation output has structure: $out/world/{world,chunks/...}
      # so we copy $out (not $out/world) to preserve the world/ subdirectory that
      # the game expects at res/worlds/<name>/world/.
      mkdir -p res/worlds
      cp -r ${dragonvoid-world-arena} res/worlds/arena
      cp -r ${dragonvoid-world-smalltest} res/worlds/smalltest
      cp -r ${dragonvoid-world-lennartswelt} res/worlds/lennartswelt

      # Build the JAR – skip world-generation Gradle tasks (worlds already present)
      gradle --no-daemon --offline jar \
        -x generateWorldArena -x generateWorldSmalltest -x generateWorldLennartswelt
    '';

    installPhase = ''
      mkdir -p $out
      cp build/libs/dragonvoid-*.jar $out/DragonVoid.jar
    '';
  };

  mkWrapper = name: mainClass: pkgs.writeShellScriptBin name ''
    JAR="${dragonvoid-jar}/DragonVoid.jar"
    SAVE_DIR="''${SAVE_DIR:-$HOME/.dragonvoid}"
    WORK_DIR=$(mktemp -d)
    trap 'rm -rf "$WORK_DIR"' EXIT

    # Initialize save directory from JAR on first run
    if [ ! -d "$SAVE_DIR" ]; then
      mkdir -p "$SAVE_DIR"
      cd "$WORK_DIR"
      ${pkgs.unzip}/bin/unzip -q "$JAR" "res/saves/*"
      cp -r "$WORK_DIR/res/saves/"* "$SAVE_DIR/"
    fi
    # Always refresh the model save template (it is config, not user data)
    cd "$WORK_DIR"
    ${pkgs.unzip}/bin/unzip -q -o "$JAR" "res/saves/model/*"
    mkdir -p "$SAVE_DIR/model"
    cp -r "$WORK_DIR/res/saves/model/." "$SAVE_DIR/model/"

    ${jre}/bin/java -Ddragonvoid.savedir="$SAVE_DIR" -cp "$JAR" ${mainClass}
  '';

  dragonvoid = pkgs.symlinkJoin {
    name = "dragonvoid";
    paths = [
      (mkWrapper "dragonvoid" "tbs.StartMainMenu")
      (mkWrapper "dragonvoid-arena" "tbs.StartArenaMode")
    ];
  };
in
{
  inherit dragonvoid dragonvoid-jar dragonvoid-world-arena dragonvoid-world-smalltest dragonvoid-world-lennartswelt;
}
