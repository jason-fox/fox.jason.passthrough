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

Files marked as `format="NEW_FORMAT"` should be processed by the `macrodef` called `do-something`. The macro must offer
an interface with `src`, `dest` and `title` attributes.

```xml
<macrodef name="do-something">
    <attribute name="src" />
    <attribute name="dest" />
    <attribute name="title" />
    <sequential>
        <!-- Further processing -->
    </sequential>
</macrodef>
```