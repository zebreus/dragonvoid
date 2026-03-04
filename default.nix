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

  dragonvoid-jar = pkgs.stdenv.mkDerivation {
    pname = "dragonvoid-jar";
    version = "0.1.0";
    src = pkgs.lib.cleanSource ./.;

    nativeBuildInputs = [ gradle jdk ];

    buildPhase = ''
      export GRADLE_USER_HOME=$(mktemp -d)
      export HOME=$(mktemp -d)

      # Set up a local flat-dir repository with pre-fetched dependencies
      mkdir -p libs
      cp ${gson} libs/gson-2.8.9.jar
      cp ${json-java} libs/json-20211205.jar

      # Build the JAR using Gradle in offline mode with local deps
      gradle --no-daemon --offline jar
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

    cd "$WORK_DIR"
    ${pkgs.unzip}/bin/unzip -q "$JAR" "res/*" -d "$WORK_DIR"

    if [ ! -d "$SAVE_DIR" ]; then
      mkdir -p "$SAVE_DIR"
      cp -r "$WORK_DIR/res/saves/"* "$SAVE_DIR"
    fi
    rm -rf "$WORK_DIR/res/saves"
    ln -s "$SAVE_DIR" "$WORK_DIR/res/saves"

    ${jre}/bin/java -cp "$JAR" ${mainClass}
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
  inherit dragonvoid dragonvoid-jar;
}
