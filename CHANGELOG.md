# Changelog

## [0.2.0](https://github.com/mayekukhisa/scaffold-cli/compare/v0.1.0...v0.2.0) (2025-03-24)


### ⚠ BREAKING CHANGES

* The configuration property `template.collection.path` is now `templates.home`.
* Handle template file types as a single `files` list in template manifest and use a single `path` property to handle both source and target paths in project file generation. This change expects a leading '_' in template path to indicate a hidden target file/directory, and '.ftl' ending to identify a FreeMarker file for preprocessing.

### Features

* rename template collection path property to `templates.home` ([7283044](https://github.com/mayekukhisa/scaffold-cli/commit/728304421dcc8c548a66bb99bed3dfda92a84fba))
* simplify template file handling and path processing ([e645489](https://github.com/mayekukhisa/scaffold-cli/commit/e645489cdb89b4f90c5dec7bff53cff16912cb4b))


### Continuous Integration

* set next release version to 0.2.0 ([c988758](https://github.com/mayekukhisa/scaffold-cli/commit/c988758850a35b71338a82a6b6dae09292cc1e35))

## [0.1.0](https://github.com/mayekukhisa/scaffold-cli/compare/v0.1.0...v0.1.0) (2024-06-10)


### Features

* add `--name` and `--package` options to `create` command ([cc0bb8b](https://github.com/mayekukhisa/scaffold-cli/commit/cc0bb8b8959dd634c3ca47eb6e24fb963dbde942))
* add command to create project structures from templates ([2f9824d](https://github.com/mayekukhisa/scaffold-cli/commit/2f9824dd72ccc695d131d9f8c0b5d858f9162470))
* add command to manage tool configurations ([218b11c](https://github.com/mayekukhisa/scaffold-cli/commit/218b11cff029278336f996fe2fc978bff085c099))
* add option to show available templates ([274ea22](https://github.com/mayekukhisa/scaffold-cli/commit/274ea2256f156699bad2a44e742b70ab5dd9c3eb))
* add support for freemarker and binary files ([cc0bb8b](https://github.com/mayekukhisa/scaffold-cli/commit/cc0bb8b8959dd634c3ca47eb6e24fb963dbde942))
* enable multi-level parent directory search for templates ([425b66c](https://github.com/mayekukhisa/scaffold-cli/commit/425b66c2b849276c608dc3a0d8f706fa8a4e0cd7))


### Continuous Integration

* initialize version at 0.1.0 ([ff5e725](https://github.com/mayekukhisa/scaffold-cli/commit/ff5e725583f1d4407eaf6821d0c0019171b511c3))
