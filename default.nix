{ pkgs ? import <nixpkgs> { }
, jdk ? pkgs.jdk8
, jre ? pkgs.jre8
}:

let
  gson = pkgs.fetchurl {
    url = "https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.9/gson-2.8.9.jar";
    sha256 = "sha256-ixNEokViKuFaLjuSMrvJLSWnUfGRqnOxEqFaDQsOj/Q=";
  };
  json-java = pkgs.fetchurl {
    url = "https://repo1.maven.org/maven2/org/json/json/20211205/json-20211205.jar";
    sha256 = "sha256-MBq8e0UJMEEmujic1ye4yEQsGMCFVGp5UsoHYJcMaEI=";
  };

  dragonvoid-jar = pkgs.stdenv.mkDerivation {
    pname = "dragonvoid-jar";
    version = "0.1.0";
    src = ./.;

    nativeBuildInputs = [ jdk pkgs.gnutar pkgs.gzip ];

    buildPhase = ''
      # Compile Java sources
      find src -name '*.java' > /tmp/sources.txt
      mkdir -p build/classes
      ${jdk}/bin/javac \
        -source 8 -target 8 \
        -encoding ISO-8859-1 \
        -cp "${gson}:${json-java}" \
        -d build/classes \
        @/tmp/sources.txt

      # Create JAR with compiled classes and resources
      mkdir -p build/jar
      cp -r build/classes/* build/jar/
      cp -r res build/jar/

      # Extract world chunk data
      tar xzf build/jar/res/worlds.tar.gz -C build/jar/
      rm build/jar/res/worlds.tar.gz

      # Create manifest
      echo "Manifest-Version: 1.0" > build/MANIFEST.MF
      echo "Main-Class: tbs.StartMainMenu" >> build/MANIFEST.MF

      cd build/jar
      ${jdk}/bin/jar cfm ../DragonVoid.jar ../MANIFEST.MF .
    '';

    installPhase = ''
      mkdir -p $out
      cp build/DragonVoid.jar $out/DragonVoid.jar
    '';
  };

  dragonvoid = pkgs.stdenv.mkDerivation {
    pname = "dragonvoid";
    version = "0.1.0";
    src = ./.;

    nativeBuildInputs = [ pkgs.makeWrapper ];

    buildInputs = [ dragonvoid-jar jre pkgs.coreutils ];

    installPhase = ''
      mkdir -p $out/lib
      mkdir -p $out/bin
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
