/*
Process the input file as a PlantUML file and output it to the provided file name.

To correctly process PlantUML file inclusion directives, this needs to be executed with the working directory set to
the directory containing the input file argument.
*/

import java.util.stream.Collectors;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceStringReader;

for (int i=0; i<args.length; i+=2){
    File inputFile = new File(args[i])
    File outputFile = new File(args[i+1])
    exportImage(inputFile, outputFile);
}

String readAllLines(File sourceFile) {
  FileInputStream inputStream = new FileInputStream(sourceFile);
  InputStreamReader reader = new InputStreamReader(inputStream);
  BufferedReader bufferedReader = new BufferedReader(reader);
  String content = bufferedReader.lines().collect(Collectors.joining("\n"));
  bufferedReader.close();

  return content;
}

int getImageCount(File plantUmlFile) {
  String content = readAllLines(plantUmlFile);
  SourceStringReader reader = new SourceStringReader(content);
  int counter = 0;
  for (BlockUml b : reader.getBlocks()) {
    final Diagram system = b.getDiagram();
    counter += system.getNbImages();
  }
  return counter;
}

void exportImage(File plantUmlFile, File outputFile) {
  System.setProperty("user.dir", plantUmlFile.parentFile.canonicalPath)
  System.out.println("Input file ${plantUmlFile.name} -> output file ${outputFile.name}")
  String content = readAllLines(plantUmlFile);
  SourceStringReader reader = new SourceStringReader(content);
  FileOutputStream os = new FileOutputStream(outputFile);
  reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
  os.close();
}
