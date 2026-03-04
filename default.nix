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

  dragonvoid = pkgs.stdenv.mkDerivation {
    pname = "dragonvoid";
    version = "0.1.0";

    dontUnpack = true;

    nativeBuildInputs = [ pkgs.makeWrapper ];

    installPhase = ''
      mkdir -p $out/lib $out/bin
      cp ${dragonvoid-jar}/DragonVoid.jar $out/lib/

      cat <<'WRAPPER' > $out/bin/dragonvoid
      #!/usr/bin/env bash
      SAVE_DIR=''${SAVE_DIR-"$HOME/.dragonvoid/"}
      WORK_DIR=$(mktemp -d)
      cd "$WORK_DIR"
      ${pkgs.unzip}/bin/unzip -q $out/lib/DragonVoid.jar "res/*" -d "$WORK_DIR"
      if ! test -e "$SAVE_DIR"; then
          mkdir -p "$SAVE_DIR"
          cp -r "$WORK_DIR/res/saves/"* "$SAVE_DIR"
      fi
      rm -rf "$WORK_DIR/res/saves"
      ln -s "$SAVE_DIR" "$WORK_DIR/res/saves"
      ${jre}/bin/java -cp $out/lib/DragonVoid.jar tbs.StartMainMenu
      WRAPPER

      cat <<'WRAPPER' > $out/bin/dragonvoid-arena
      #!/usr/bin/env bash
      SAVE_DIR=''${SAVE_DIR-"$HOME/.dragonvoid/"}
      WORK_DIR=$(mktemp -d)
      cd "$WORK_DIR"
      ${pkgs.unzip}/bin/unzip -q $out/lib/DragonVoid.jar "res/*" -d "$WORK_DIR"
      if ! test -e "$SAVE_DIR"; then
          mkdir -p "$SAVE_DIR"
          cp -r "$WORK_DIR/res/saves/"* "$SAVE_DIR"
      fi
      rm -rf "$WORK_DIR/res/saves"
      ln -s "$SAVE_DIR" "$WORK_DIR/res/saves"
      ${jre}/bin/java -cp $out/lib/DragonVoid.jar tbs.StartArenaMode
      WRAPPER

      chmod 755 $out/bin/dragonvoid
      chmod 755 $out/bin/dragonvoid-arena
    '';
  };
in
{
  inherit dragonvoid dragonvoid-jar;
}
