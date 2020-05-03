<h1>ANT Task</h1>

The following ANT task is available from the DITA-OT Pass Through plug-in

## Passthrough-Iterate

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

Files marked as `format="NEW_FORMAT"` should be processed by the `macrodef` called `do-something`. The macro must
offer an interface with `src`, `dest`, `title` and `metadata` attributes. All attributes will be pre-supplied by 
DITA-OT

| Attribute | Description                                             | Required |
| --------- | ------------------------------------------------------- | -------- |
| src       | The source file which holds the raw input format        | Yes      |
| dest      | The destination file for the processed DITA             | Yes      |
| title     | The title to use for the DITA topic                     | Yes      |
| metadata  | Any addtional topic metadata to process (in XML format) | Yes      |

```xml
<macrodef name="do-something">
    <attribute name="src" />
    <attribute name="dest" />
    <attribute name="title" />
    <attribute name="metadata" />
    <sequential>
        <!-- Further processing -->
    </sequential>
</macrodef>
```
