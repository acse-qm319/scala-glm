import scalaglm._
import breeze.linalg._

// https://archive.ics.uci.edu/ml/datasets/banknote+authentication
val url = "https://archive.ics.uci.edu/ml/machine-learning-databases/00267/data_banknote_authentication.txt"
val fileName = "banknote.csv"

// download the file to disk if it hasn't been already
val file = new java.io.File(fileName)
if (!file.exists) {
  val s = new java.io.PrintWriter(file)
  val data = scala.io.Source.fromURL(url).getLines
  data.foreach(l => s.write(l+"\n"))
  s.close
}

// read the file from disk
val mat = csvread(new java.io.File(fileName))
println("Dim: " + mat.rows + " " + mat.cols)
val y = mat(::, 4) // response is the final column
val X = mat(::, 0 to 3)
val mod = Glm(y, X, List("Var","Skew","Kurt","Entropy"), LogisticGlm)
mod.summary


// eof

