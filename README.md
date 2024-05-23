# Scaffold

Scaffold is a project structure generator tool that automates the setup of new projects, allowing developers to quickly focus on building their applications.

## Key Features

> **Note:** This tool is in early development and currently has limited features.

## Getting Started

This section will guide you through obtaining a copy of the project and running the tool on your local machine.

### System Requirements

Before running this project, ensure the following software is installed on your system:

-  JDK 17

### Installation

> **Note:** Use **PowerShell** or **Git Bash** to execute commands on Windows.

1. Clone the repository to your local machine:

   ```shell
   git clone https://github.com/mayekukhisa/scaffold.git
   ```

2. Navigate to the project directory:

   ```shell
   cd scaffold
   ```

3. Install the project as a distribution:

   ```shell
   ./gradlew installDist
   ```

4. Run the tool from the build directory:

   ```shell
   ./build/install/scaffold/bin/scaffold
   ```

Alternatively, skip steps 2 through 4 and open the project in [IntelliJ IDEA][1]. For more on how to build and run the tool, please consult the [guide][2].

## License

This project is available under the terms of the [GPL-3.0 license][3].

&copy; 2024 Mayeku Khisa.

[1]: https://www.jetbrains.com/idea/
[2]: https://www.jetbrains.com/help/idea/running-applications.html
[3]: LICENSE
