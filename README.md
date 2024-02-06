### How to build

- Install [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)

- Export jar
  ![export-jar](./img/export-jar.png)

- Select main class
  ![select-main-class](./img/select-main-class.png)

- ContextMenu.jar
  ![ContextMenu.jar](./img/ContextMenu.jar.png)

### Modify the Registry

1. Open the Registry Editor by typing `regedit` in the Run Box
2. Navigate to `HKEY_CLASSES_ROOT\*\shell`
3. Create a new key under `shell` named `Code challenge`
4. Under the key `Code challenge`, create another new key named `command`
5. Set the default value of the `command` key to `"<path-to-java.exe>" -jar "<path-to-ContextMenu.jar>" "%1"`, e.g. `"C:\\Program Files\\Java\\jdk-17\\bin\\java.exe" -jar "C:\\Users\\xinyi.han\\code\\ContextMenu\\ContextMenu.jar" "%1"`

### Demo

- Select a single file
  ![select-single-file](./img/select-single-file.png)

- Select multiple files
  ![select-multiple-files](./img/select-multiple-files.png)

- Show file(s) information
  ![file(s)-information](<./img/file(s)-information.png>)
