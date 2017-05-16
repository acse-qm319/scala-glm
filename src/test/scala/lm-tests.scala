/*
lm-tests.scala

Test code for regression modelling

 */

package scalaglm

import org.scalatest.FlatSpec
import breeze.linalg._
import breeze.numerics._


class LmSpec extends FlatSpec {

  "1+1" should "be 2" in {
    assert(1+1 === 2)
  }

  import Utils._

  "backSolve" should "invert correctly (1)" in {
    val A = DenseMatrix((4,1),(0,2)) map (_.toDouble)
    val x = DenseVector(3.0,-2.0)
    val y = A * x
    val xx = backSolve(A,y)
    assert (norm(x-xx) < 0.00001)
  }

  it should "invert correctly (2)" in {
    val A = DenseMatrix((42,11),(0,8)) map (_.toDouble)
    val x = DenseVector(7.0,-3.0)
    val y = A * x
    val xx = backSolve(A,y)
    assert (norm(x-xx) < 0.00001)
  }

  "Lm" should "handle 2 points on a horizontal line (manual intercept)" in {
    val y = DenseVector(5.0,5.0)
    val x = DenseMatrix((1.0,2.0),(1.0,4.0))
    val mod = Lm(y,x,List("Intercept","x"),false)
    val beta = DenseVector(5.0,0.0)
    assert(norm(mod.coefficients - beta) < 0.00001)
  }

  it should "handle 2 points on a slope (manual intercept)" in {
    val y = DenseVector(2.0,3.0)
    val x = DenseMatrix((1.0,2.0),(1.0,4.0))
    val mod = Lm(y,x,List("Intercept","x"),false)
    val beta = DenseVector(1.0,0.5)
    assert(norm(mod.coefficients - beta) < 0.00001)
    assert(abs(mod.rSquared - 1.0) < 0.00001)
  }

  it should "handle 3 points on a diagonal (manual intercept)" in {
    val y = DenseVector(4.0,5.0,6.0)
    val x = DenseMatrix((1.0,2.0),(1.0,3.0),(1.0,4.0))
    val mod = Lm(y,x,List("Intercept","x"),false)
    val beta = DenseVector(2.0,1.0)
    assert(norm(mod.coefficients - beta) < 0.00001)
    assert(abs(mod.rSquared - 1.0) < 0.00001)
  }

  it should "handle 2 points on a horizontal line (auto intercept)" in {
    val y = DenseVector(5.0,5.0)
    val x = DenseMatrix((2.0),(4.0))
    val mod = Lm(y,x,List("x"))
    val beta = DenseVector(5.0,0.0)
    assert(norm(mod.coefficients - beta) < 0.00001)
  }

  it should "handle 2 points on a slope (auto intercept)" in {
    val y = DenseVector(2.0,3.0)
    val x = DenseMatrix((2.0),(4.0))
    val mod = Lm(y,x,List("x"))
    val beta = DenseVector(1.0,0.5)
    assert(norm(mod.coefficients - beta) < 0.00001)
    assert(abs(mod.rSquared - 1.0) < 0.00001)
  }

  it should "handle 3 points on a diagonal (auto intercept)" in {
    val y = DenseVector(4.0,5.0,6.0)
    val x = DenseMatrix((2.0),(3.0),(4.0))
    val mod = Lm(y,x,List("x"))
    val beta = DenseVector(2.0,1.0)
    assert(norm(mod.coefficients - beta) < 0.00001)
    assert(abs(mod.rSquared - 1.0) < 0.00001)
  }

  it should "fit a simple linear regression model and get the same as R" in {
    val y = DenseVector(1.0,2.5,0.5,3.0)
    val x = DenseMatrix((1.0),(2.5),(3.0),(2.0))
    val mod = Lm(y,x,List("Covariate"))
    //mod.summary
    val R = org.ddahl.rscala.RClient()
    R.y = y.toArray
    R.x = x(::,0).toDenseVector.toArray
    R.eval("mod = lm(y~x)")
    val rCoef = DenseVector[Double](R.evalD1("mod$coefficients"))
    assert(norm(mod.coefficients - rCoef) <= 0.00001)
    val rSe = DenseVector[Double](R.evalD1("summary(mod)$coefficients[,2]"))
    assert(norm(mod.se - rSe) <= 0.00001)
    val rT = DenseVector[Double](R.evalD1("summary(mod)$coefficients[,3]"))
    assert(norm(mod.t - rT) <= 0.00001)
    val rP = DenseVector[Double](R.evalD1("summary(mod)$coefficients[,4]"))
    assert(norm(mod.p - rP) <= 0.00001)
  }








}

// eof


