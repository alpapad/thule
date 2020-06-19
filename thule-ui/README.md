# How to install UI Bakery generated code

* Unzip uibakery source code into src/main/webapp
* Edit src/main/webapp/angular.json to change outputPath to "dist/static"
* Edit src/main/webapp/angular.json to add projects->EducationManagementWebApp->architect->build->options->baseHref = "/thule/ui/"
* Edit src/main/webapp/angular.json to add projects->EducationManagementWebApp->architect->build->options->rebaseRootRelativeCssUrls = true
* Search and replace all "/assets/" with "~/../assets/" in src/main/webapp except those for "background-image: url("