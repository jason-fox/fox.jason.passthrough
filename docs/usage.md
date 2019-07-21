<h1>Usage</h1>

To mark a file as requiring no processing, label it with `format="passthrough"` within the `*.ditamap` as shown:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE bookmap PUBLIC "-//OASIS//DTD DITA BookMap//EN" "bookmap.dtd">
<bookmap>
    ...etc
    <mapref format="passthrough" href="sample.txt"/>
</bookmap>
```

The additional file will be added to the build job without processing.

## Extension points

This plug-in is designed to be extended and offers two extension points:

-   `fox.jason.passthrough.pre` - Runs an additional Ant target before the passthrough pre-processing stage.

-   `fox.jason.passthrough.process` - Runs an additional Ant target as part of the passthrough processing stage.

### Example

The following `plugin.xml` will enable the post-processing of all files marked as `format=NEW_FORMAT`

#### `plugin.xml` Configuration

```xml
<plugin id="com.example.passthrough.dita">
  <feature extension="fox.jason.passthrough.process" value="run-processing"/>
  <feature extension="dita.parser">
    <parser format="NEW_FORMAT" class="fox.jason.passthrough.parser.FileReader"/>
  </feature>
</plugin>
```

#### ANT build file

```xml
<project name="com.example.passthrough.dita">

  <macrodef name="do-something">
    <attribute name="src" />
    <attribute name="dest" />
    <attribute name="title" />
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