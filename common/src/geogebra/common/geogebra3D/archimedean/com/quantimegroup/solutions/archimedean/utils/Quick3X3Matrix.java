/*
Archimedean 1.1, a 3D applet/application for visualizing, building, 
transforming and analyzing Archimedean solids and their derivatives.
Copyright 1998, 2011 Raffi J. Kasparian, www.raffikasparian.com.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package geogebra.common.geogebra3D.archimedean.com.quantimegroup.solutions.archimedean.utils;public class Quick3X3Matrix extends Matrix {	private static Quick3X3Matrix identity = new Quick3X3Matrix(OrderedTriple.xAxis(), OrderedTriple.yAxis(), OrderedTriple.zAxis());	private Quick3X3Matrix(double[][] m) {		mat = m;	}	Quick3X3Matrix() {		mat = new double[3][3];	}	Quick3X3Matrix(Quick3X3Matrix m) {		this();		for (int i = 0; i < 3; ++i) {			System.arraycopy(m.mat[i], 0, mat[i], 0, 3);		}	}	Quick3X3Matrix(OrderedTriple p1, OrderedTriple p2, OrderedTriple p3) {		this();		mat[0][0] = p1.x;		mat[1][0] = p1.y;		mat[2][0] = p1.z;		mat[0][1] = p2.x;		mat[1][1] = p2.y;		mat[2][1] = p2.z;		mat[0][2] = p3.x;		mat[1][2] = p3.y;		mat[2][2] = p3.z;	}	double determinant() {		return mat[0][0] * mat[1][1] * mat[2][2] + mat[0][1] * mat[1][2] * mat[2][0] + mat[0][2] * mat[1][0] * mat[2][1] - mat[0][2]				* mat[1][1] * mat[2][0] - mat[1][2] * mat[2][1] * mat[0][0] - mat[2][2] * mat[0][1] * mat[1][0];	}	Quick3X3Matrix adjoint() {		double[] x = {				mat[1][1] * mat[2][2] - mat[1][2] * mat[2][1], mat[0][2] * mat[2][1] - mat[0][1] * mat[2][2],				mat[0][1] * mat[1][2] - mat[0][2] * mat[1][1] };		double[] y = {				mat[1][2] * mat[2][0] - mat[1][0] * mat[2][2], mat[0][0] * mat[2][2] - mat[0][2] * mat[2][0],				mat[0][2] * mat[1][0] - mat[0][0] * mat[1][2] };		double[] z = {				mat[1][0] * mat[2][1] - mat[1][1] * mat[2][0], mat[0][1] * mat[2][0] - mat[0][0] * mat[2][1],				mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0] };		double[][] m = {				x, y, z };		return new Quick3X3Matrix(m);	}	protected Quick3X3Matrix inverse() {		Quick3X3Matrix adjoint = adjoint();		adjoint.timesEquals(1 / determinant());		return adjoint;	}	static double determinant(OrderedTriple p1, OrderedTriple p2, OrderedTriple p3) {		return p1.x * p2.y * p3.z + p2.x * p3.y * p1.z + p3.x * p1.y * p2.z - p3.x * p2.y * p1.z - p3.y * p2.z * p1.x - p3.z * p2.x * p1.y;	}	static Quick3X3Matrix adjoint(OrderedTriple p1, OrderedTriple p2, OrderedTriple p3) {		double[] x = {				p2.y * p3.z - p3.y * p2.z, p3.x * p2.z - p2.x * p3.z, p2.x * p3.y - p3.x * p2.y };		double[] y = {				p3.y * p1.z - p1.y * p3.z, p1.x * p3.z - p3.x * p1.z, p3.x * p1.y - p1.x * p3.y };		double[] z = {				p1.y * p2.z - p2.y * p1.z, p2.x * p1.z - p1.x * p2.z, p1.x * p2.y - p2.x * p1.y };		double[][] m = {				x, y, z };		return new Quick3X3Matrix(m);	}	static Quick3X3Matrix inverse(OrderedTriple p1, OrderedTriple p2, OrderedTriple p3) {		Quick3X3Matrix adjoint = adjoint(p1, p2, p3);		adjoint.timesEquals(1 / determinant(p1, p2, p3));		return adjoint;	}	void timesEquals(double d) {		mat[0][0] *= d;		mat[1][0] *= d;		mat[2][0] *= d;		mat[0][1] *= d;		mat[1][1] *= d;		mat[2][1] *= d;		mat[0][2] *= d;		mat[1][2] *= d;		mat[2][2] *= d;	}	Quick3X3Matrix timesEqualsOld(double d) {		mat[0][0] *= d;		mat[1][0] *= d;		mat[2][0] *= d;		mat[0][1] *= d;		mat[1][1] *= d;		mat[2][1] *= d;		mat[0][2] *= d;		mat[1][2] *= d;		mat[2][2] *= d;		return this;	}	protected Quick3X3Matrix times(Quick3X3Matrix B) {		// A * B = C		Quick3X3Matrix A = this;		Quick3X3Matrix C = new Quick3X3Matrix();		C.mat[0][0] = A.mat[0][0] * B.mat[0][0] + A.mat[0][1] * B.mat[1][0] + A.mat[0][2] * B.mat[2][0];		C.mat[1][0] = A.mat[1][0] * B.mat[0][0] + A.mat[1][1] * B.mat[1][0] + A.mat[1][2] * B.mat[2][0];		C.mat[2][0] = A.mat[2][0] * B.mat[0][0] + A.mat[2][1] * B.mat[1][0] + A.mat[2][2] * B.mat[2][0];		C.mat[0][1] = A.mat[0][0] * B.mat[0][1] + A.mat[0][1] * B.mat[1][1] + A.mat[0][2] * B.mat[2][1];		C.mat[1][1] = A.mat[1][0] * B.mat[0][1] + A.mat[1][1] * B.mat[1][1] + A.mat[1][2] * B.mat[2][1];		C.mat[2][1] = A.mat[2][0] * B.mat[0][1] + A.mat[2][1] * B.mat[1][1] + A.mat[2][2] * B.mat[2][1];		C.mat[0][2] = A.mat[0][0] * B.mat[0][2] + A.mat[0][1] * B.mat[1][2] + A.mat[0][2] * B.mat[2][2];		C.mat[1][2] = A.mat[1][0] * B.mat[0][2] + A.mat[1][1] * B.mat[1][2] + A.mat[1][2] * B.mat[2][2];		C.mat[2][2] = A.mat[2][0] * B.mat[0][2] + A.mat[2][1] * B.mat[1][2] + A.mat[2][2] * B.mat[2][2];		return C;	}	static Quick3X3Matrix findRotationMatrix(OrderedTriple p1, OrderedTriple p2, OrderedTriple p3, OrderedTriple n1, OrderedTriple n2,			OrderedTriple n3) {		return (new Quick3X3Matrix(n1, n2, n3)).times(Quick3X3Matrix.inverse(p1, p2, p3));	}	public static Quick3X3Matrix findRotationMatrix(OrderedTriple p1, OrderedTriple n1, OrderedTriple p2, OrderedTriple n2) {		// find a matrix M that will rotate the coordinate space around the origin		// to bring p1 -> n1 and p2 -> n2		double epsilon = 1e-5;		if (n1.isApprox(p1, epsilon) && n2.isApprox(p2, epsilon)) {			return Quick3X3Matrix.identity;		}		OrderedTriple p3 = p2.cross(p1);		OrderedTriple n3 = n2.cross(n1);		return (new Quick3X3Matrix(n1, n2, n3)).times(Quick3X3Matrix.inverse(p1, p2, p3));	}	public static Quick3X3Matrix findRotationMatrix(OrderedTriple p1, OrderedTriple n1) {		// find a matrix M that will rotate the coordinate space around the origin		// to bring p1 -> n1		double epsilon = 1e-5;		if (n1.isApprox(p1, epsilon)) {			return Quick3X3Matrix.identity;		}		OrderedTriple p2 = n1.isApprox(p1.negative(), epsilon) ? OrderedTriple.yAxis() : p1.cross(n1);		OrderedTriple n2 = p2;		OrderedTriple p3 = p2.cross(p1);		OrderedTriple n3 = n2.cross(n1);		return (new Quick3X3Matrix(n1, n2, n3)).times(Quick3X3Matrix.inverse(p1, p2, p3));	}	public OrderedTriple times(OrderedTriple p) {		OrderedTriple np = new OrderedTriple();		np.x = mat[0][0] * p.x + mat[0][1] * p.y + mat[0][2] * p.z;		np.y = mat[1][0] * p.x + mat[1][1] * p.y + mat[1][2] * p.z;		np.z = mat[2][0] * p.x + mat[2][1] * p.y + mat[2][2] * p.z;		return np;	}	public String toString() {		return toString(mat[0]) + "\n" + toString(mat[1]) + "\n" + toString(mat[2]);	}	public static String toString(double[] d) {		StringBuffer buf = new StringBuffer("(");		for (int i = 0; i < d.length; ++i) {			buf.append(d[i]);			if (i < d.length - 1) {				buf.append(", ");			}		}		buf.append(")");		return buf.toString();	}}