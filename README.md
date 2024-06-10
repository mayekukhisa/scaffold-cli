# Scaffold

Scaffold is a project structure generator tool that automates the setup of new projects, allowing developers to dive straight into building their applications without the hassle of initial configuration.

## Key Features

-  **Project Generation**: Quickly create new projects using predefined templates.

## Getting Started

This section will guide you through setting up the tool on your local machine.

### System Requirements

Before running this tool, ensure the following software is installed on your system:

-  JRE or JDK 17, or newer. You can download the latest version from [Adoptium][1].

### Installation

1. Download the [latest release][2].

2. Extract the downloaded file (.zip or .tar.gz) to your preferred location.

3. Set the PATH variable:

   <details>
   <summary>Windows</summary>

   -  Right-click on "**This PC**" or "**My Computer**" and select "**Properties**".

   -  Navigate to "**Advanced system settings**" &rarr; "**Advanced**" tab &rarr; "**Environment Variables**".

   -  Under "**User variables**", locate the "**Path**" variable. If it doesn't exist, create it by clicking "**New**", naming it "**Path**", and setting its value.

   -  If "**Path**" exists, select it and click "**Edit**" &rarr; "**New**", then add the absolute path to the `bin` directory of your extracted file.

   -  Click "**OK**" on each open window to save your changes.
   </details>

   <details>
   <summary>Linux and macOS</summary>

   -  Add the following line to your `~/.bashrc` (for bash) or `~/.zshrc` (for zsh):

      ```shell
      export PATH="$PATH:/path/to/bin"
      ```

      Replace `/path/to/bin` with the absolute path to the `bin` directory of your extracted file.

   -  Apply changes by running `source ~/.bashrc` (for bash) or `source ~/.zshrc` (for zsh). Alternatively, restart your terminal to apply the changes.

     </details>

4. Verify installation:

   ```shell
   scaffold --version
   ```

5. Follow the steps outlined [here][3] to configure your template collection.

### Generating a Project

1. Run the following command to list all available templates:

   ```shell
   scaffold --list-templates
   ```

   You can find an overview of each template [here][4].

2. Create a new project using the desired template with the following command:

   ```shell
   scaffold create -t <template> <directory>
   ```

   Replace `<template>` with the name of the template you've chosen, and `<directory>` with the desired name for your project's directory.

   Run `scaffold create --help` for a full list of options available for this command.

## License

This project is available under the terms of the [GPL-3.0 license][5].

&copy; 2024 Mayeku Khisa.

[1]: https://adoptium.net
[2]: https://github.com/mayekukhisa/scaffold/releases/latest
[3]: https://github.com/mayekukhisa/scaffold-template-collection#installation
[4]: https://github.com/mayekukhisa/scaffold-template-collection#available-templates
[5]: LICENSE
