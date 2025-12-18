// package com.dluca22.FileController;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import java.io.File;

// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;

// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
// public class FileControllerTest {

//   private String baseDir;
//   private File file1;
//   private FileController controller;

//   @BeforeAll
//   void setup() {
//     this.baseDir = "/workspace/testFiles";
//     this.file1 = new File(this.baseDir + "/Juan Ciu Fri - 397.zip");
//     this.controller = new FileController(baseDir);
//   }

//   @Test
//   @DisplayName("createTargetDirectory produces expected folder name")
//   void testCreateTargetDirectory() {
//     String expected = this.baseDir + "/Juan_Ciu_Fri_-_397";
//     try {

//       String actual = controller.createTargetDirectory(file1);

//       assertEquals(expected, actual);
//     } catch (Exception e) {
//       System.out.println("Failed");
//     }

//   }
// }
