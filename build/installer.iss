[Setup]
AppName=Chore List Generator
AppVersion=1.0
AppPublisher=StarFutures
DefaultDirName={autopf}\ChoreListGenerator
DefaultGroupName=Chore List Generator
UninstallDisplayIcon={app}\chore-list-generator.exe
OutputDir=.
OutputBaseFilename=ChoreListGenerator_Installer_Windows
Compression=lzma2
SolidCompression=yes
; Uncomment the next line if you want to require admin rights
; PrivilegesRequired=admin

[Files]
; Your Launch4j exe
Source: "chore-list-generator.exe"; DestDir: "{app}"
; The bundled JRE
Source: "jre\*"; DestDir: "{app}\jre"; Flags: recursesubdirs

[Run]
; Option to launch after installation
Filename: "{app}\chore-list-generator.exe"; Description: "Launch Chore List Generator"; Flags: nowait postinstall skipifsilent