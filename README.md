# Pass Through Plugin for DITA-OT [<img src="https://jason-fox.github.io/fox.jason.passthrough/passthrough.png" align="right" width="300">](http://passthroughdita-ot.rtfd.io/)

[![license](https://img.shields.io/github/license/jason-fox/fox.jason.passthrough.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![DITA-OT 3.5](https://img.shields.io/badge/DITA--OT-3.5-blue.svg)](http://www.dita-ot.org/3.5)
[![Build Status](https://travis-ci.org/jason-fox/fox.jason.passthrough.svg?branch=master)](https://travis-ci.org/jason-fox/fox.jason.passthrough)
[![Documentation Status](https://readthedocs.org/projects/passthroughdita-ot/badge/?version=latest)](https://passthroughdita-ot.readthedocs.io/en/latest/?badge=latest)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fox.jason.passthrough&metric=alert_status)](https://sonarcloud.io/dashboard?id=fox.jason.passthrough)

This is an abstract base [DITA-OT Plug-in](https://www.dita-ot.org/plugins) to enable files to bypass DITA-OT
pre-processing and be copied directly over into the processing directory. It is designed to be extended.

The plugin consists of a no-op file reader and an Antlib library. It offers two extension-points for further processing.

<details>
<summary><strong>Table of Contents</strong></summary>

-   [Install](#install)
    -   [Installing DITA-OT](#installing-dita-ot)
    -   [Installing the Plug-in](#installing-the-plug-in)
-   [Usage](#usage)
    -   [Extension points](#extension-points)
        -   [Example](#example)
-   [API](#api)
    -   [Passthrough-Iterate](#passthrough-iterate)
-   [License](#license)

</details>

## Install

The DITA-OT Passthrough plug-in has been tested against [DITA-OT 3.x](http://www.dita-ot.org/download). It is
recommended that you upgrade to the latest version.

### Installing DITA-OT

<a href="https://www.dita-ot.org"><img src="https://www.dita-ot.org/images/dita-ot-logo.svg" align="right" height="55"></a>

The DITA-OT Pass Through plug-in is a file reader for the DITA Open Toolkit.

-   Full installation instructions for downloading DITA-OT can be found
    [here](https://www.dita-ot.org/3.5/topics/installing-client.html).

    1.  Download the `dita-ot-3.5.zip` package from the project website at
        [dita-ot.org/download](https://www.dita-ot.org/download)
    2.  Extract the contents of the package to the directory where you want to install DITA-OT.
    3.  **Optional**: Add the absolute path for the `bin` directory to the _PATH_ system variable.

    This defines the necessary environment variable to run the `dita` command from the command line.

```console
curl -LO https://github.com/dita-ot/dita-ot/releases/download/3.5/dita-ot-3.5.zip
unzip -q dita-ot-3.5.zip
rm dita-ot-3.5.zip
```

### Installing the Plug-in

-   Run the plug-in installation commands:

```console
dita install https://github.com/doctales/org.doctales.xmltask/archive/master.zip
dita install https://github.com/jason-fox/fox.jason.passthrough/archive/master.zip
```

The `dita` command line tool requires no additional configuration.

---

## Usage

To mark a file as requiring no processing, label it with `format="passthrough"` within the `*.ditamap` as shown:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE bookmap PUBLIC "-//OASIS//DTD DITA BookMap//EN" "bookmap.dtd">
<bookmap>
    ...etc
    <chapter format="passthrough" href="sample.txt"/>
</bookmap>
```

The additional file will be added to the build job without processing.

### Extension points

This plug-in is designed to be extended and offers two extension points:

-   `passthrough.pre` - Runs an additional Ant target before the passthrough pre-processing stage.

-   `passthrough.process` - Runs an additional Ant target as part of the passthrough processing stage.

#### Example

The following `plugin.xml` will enable the post-processing of all files marked as `format=NEW_FORMAT`

##### `plugin.xml` Configuration

```xml
<plugin id="com.example.passthrough.dita">
  <require plugin="fox.jason.passthrough"/>
  <feature extension="passthrough.process" value="run-processing"/>
  <feature extension="dita.parser">
    <parser format="NEW_FORMAT" class="fox.jason.passthrough.parser.FileReader"/>
  </feature>
</plugin>
```

##### ANT build file

```xml
<project name="com.example.passthrough.dita">

  <macrodef name="do-something">
    <attribute name="src" />
    <attribute name="dest" />
    <attribute name="title" />
    <attribute name="metadata"/>
    <sequential>
      <!-- Further processing -->
    </sequential>
  </macrodef>

  <target name="run-processing">
    <passthrough-iterate format="NEW_FORMAT" macro="do-something"/>
  </target>
</project>
```

A working example can be found in the [DITA-OT Pandoc plug-in](https://github.com/jason-fox/fox.jason.pandoc)
repository.

## API

The following ANT task is available from the DITA-OT Pass through plug-in

### Passthrough-Iterate

#### Description

Post process all files of a given format using the macro supplied

#### Parameters

| Attribute | Description                                      | Required |
| --------- | ------------------------------------------------ | -------- |
| format    | The `format` attribute used with the `*.ditamap` | Yes      |
| macro     | The name of the `<macrodef>` to run              | Yes      |

#### Example

```xml
 <passthrough-iterate format="NEW_FORMAT" macro="do-something"/>
```

Files marked as `format="NEW_FORMAT"` should be processed by the `macrodef` called `do-something`. The macro must offer
an interface with `src`, `dest`, `title` and `metadata` attributes.

## License

[Apache 2.0](LICENSE) © 2019 - 2020 Jason Fox

The Program includes the following additional software components which were obtained under license:

-   xmltask.jar - http://www.oopsconsultancy.com/software/xmltask/ - **Apache 1.1 license** (within
    `org.doctales.xmltask` plug-in)
