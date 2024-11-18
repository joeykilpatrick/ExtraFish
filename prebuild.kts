import java.io.*
import java.nio.file.*
import java.util.zip.*
import javax.xml.parsers.DocumentBuilderFactory

val pomFile = File("./pom.xml")
val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pomFile)
val artifactName = document.getElementsByTagName("artifactId").item(0).textContent
val version = document.getElementsByTagName("version").item(0).textContent

// 1. Generate plugin.yml

File("src/main/resources").mkdirs()

var pluginYml = File("src/main/resources/plugin.yml")

pluginYml.delete()

val config = """
name: $artifactName
version: $version
main: cloud.kilpatrick.minecraft.extrafish.ExtraFishPlugin
api-version: 1.16
"""

pluginYml.printWriter().use { out ->
    out.print(config)
}

// 2. Zip up resource pack

File("target").mkdirs()

val sourcePath = Paths.get("./resource-pack")
val zipOutput = File("target/$artifactName-$version-resource-pack.zip")

FileOutputStream(zipOutput).use { fos ->
    ZipOutputStream(fos).use { zos ->
        Files.walk(sourcePath).forEach { path ->
            val relativePath = sourcePath.relativize(path).toString()
            if (Files.isDirectory(path)) {
                if (relativePath.isNotEmpty()) {
                    zos.putNextEntry(ZipEntry("$relativePath/"))
                    zos.closeEntry()
                }
            } else {
                zos.putNextEntry(ZipEntry(relativePath))
                Files.newInputStream(path).use { input ->
                    input.copyTo(zos)
                }
                zos.closeEntry()
            }
        }
    }
}
