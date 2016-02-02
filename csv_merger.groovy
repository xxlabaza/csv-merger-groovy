
temporaryFolder = new File('./tmp')
if (!temporaryFolder.exists()) {
    temporaryFolder.mkdirs()
}

void parseFileAndCreateFoldersTree (String csvFileName, String filePrefix) {
    new File(csvFileName).eachLine { line ->
        def tokens = line.split(',')
        def folder = new File(temporaryFolder, tokens[0])
        if (!folder.exists()) {
            folder.mkdirs()
        }
        new File(folder, "$filePrefix${tokens[1]}").createNewFile()
    }
}

firstPrefix = 'A-'
secondPrefix = 'B-'

parseFileAndCreateFoldersTree('./first.csv', firstPrefix)
parseFileAndCreateFoldersTree('./second.csv', secondPrefix)


def resultFile = new File('./result.csv')

temporaryFolder.listFiles()
        .findAll { folder ->
            def fileNames = folder.listFiles().collect { it.name }
            fileNames.size() > 1 && fileNames.any { it.startsWith(firstPrefix) } && fileNames.any { it.startsWith(secondPrefix) }
        }
        .each { folder ->
            def fileNames = folder.listFiles().collect { it.name }
            def firstPrefixFileNames = fileNames.findAll { it.startsWith(firstPrefix) }.collect { it - firstPrefix }
            def secondPrefixFileNames = fileNames.findAll { it.startsWith(secondPrefix) }.collect { it - secondPrefix }

            firstPrefixFileNames.each { aName ->
                secondPrefixFileNames.each { bName ->
                    resultFile << "$folder.name,$aName,$bName\n"
                }
            }
        }
