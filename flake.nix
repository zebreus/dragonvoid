{
  description = "An old (2017) unfinished game project from school";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.05";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        builds = (import ./. ({
          pkgs = pkgs;
        }));
      in
      {
        packages.dragonvoid-jar = builds.dragonvoid-jar;
        packages.default = builds.dragonvoid;

        apps.main = flake-utils.lib.mkApp {
          drv = builds.dragonvoid;
          exePath = "/bin/dragonvoid";
        };
        apps.arena = flake-utils.lib.mkApp {
          drv = builds.dragonvoid;
          exePath = "/bin/dragonvoid-arena";
        };
        apps.default = self.apps.${system}.main;

        devShells.default = pkgs.mkShell {
          buildInputs = [ pkgs.jdk17 pkgs.gradle ];
        };
      }
    );
}
