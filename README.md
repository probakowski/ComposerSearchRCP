# Composer Search RCP
## Description
This project provides standalone client for Packagist (https://packagist.org/) and other Composer (https://getcomposer.org/) repositories.
## Installation
Prerequisites: instaled Maven 3 and Java6 or higher.
1. Check out all files
2. Invoke mvn clean verify in pl.robakowski.composer.releng
3. Ready-to-use applications will be available in pl.robakowski.composer.updatesite/target/products(currently it will build for Windows 32 and 64 bits and Linux gtk 32 and 64 bits)
You can also install plugin in your Eclipse editor using local p2 repository located in pl.robakowski.composer.updatesite/target/repository
## Usage
1. In application search window is already started. In Eclipse you can click on Composer icon on main toolbar.
2. Just type query and results will be shown.
3. You can manage repositories that are used for search in Window->Preferences->Composer Search menu. Packagist doesn't require php or composer installed but any other will so you have to configure paths for it.