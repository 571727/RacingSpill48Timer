package engine.math;

import java.util.Arrays;

public class Matrix4f {

	public static final int SIZE = 4;
	private float[] elements = new float[SIZE * SIZE]; // Really a 2d array

	/**
	 * 1000 0100 0010 0001
	 */
	public static Matrix4f identity() {
		Matrix4f result = new Matrix4f();

		for(int i = 0; i < 4; i++) {
			result.set(i, i, 1);
		}

		return result;
	}

	/**
	 * 100x 010y 001z 0001
	 */
	public static Matrix4f translate(Vector3f translate) {
		Matrix4f result = Matrix4f.identity();

		for(int i = 0; i < 3; i++) {
			result.set(3, i, translate.get(i));
		}
		
		return result;
	}

	/**
	 * @param angle is how much you want to rotate the object
	 * @param axis  how were gonna rotate it (x or y or z)
	 */
	public static Matrix4f rotate(float angle, Vector3f axis) {
		Matrix4f result = Matrix4f.identity();

		double cos = Math.cos(Math.toRadians(angle));
		double sin = Math.sin(Math.toRadians(angle));
		double arcCos = 1 - cos;

		result.set(0, 0, cos + (axis.x() * axis.x()) * arcCos);
		result.set(0, 1, axis.x() * axis.y() * arcCos - axis.z() * sin);
		result.set(0, 2, axis.x() * axis.z() * arcCos + axis.y() * sin);
		result.set(1, 0, axis.y() * axis.x() * arcCos + axis.z() * sin);
		result.set(1, 1, cos + (axis.y() * axis.y()) * arcCos);
		result.set(1, 2, axis.y() * axis.z() * arcCos - axis.x() * sin);
		result.set(2, 0, axis.z() * axis.x() * arcCos - axis.y() * sin);
		result.set(2, 1, axis.z() * axis.y() * arcCos + axis.x() * sin);
		result.set(2, 2, cos + (axis.z() * axis.z()) * arcCos);

		return result;
	}

	/**
	 * x000 0y00 00z0 0001
	 */
	public static Matrix4f scale(Vector3f scale) {
		Matrix4f result = Matrix4f.identity();

		for (int i = 0; i < 3; i++) {
			result.set(i, i, scale.get(i));
		}

		return result;
	}

	public static Matrix4f transform(Vector3f position, Vector3f rotation, Vector3f scale) {
		Matrix4f result = Matrix4f.identity();

		Matrix4f translationMatrix = Matrix4f.translate(position);
		Matrix4f rotXMatrix = Matrix4f.rotate(rotation.x(), new Vector3f(1, 0, 0));
		Matrix4f rotYMatrix = Matrix4f.rotate(rotation.y(), new Vector3f(0, 1, 0));
		Matrix4f rotZMatrix = Matrix4f.rotate(rotation.z(), new Vector3f(0, 0, 1));
		Matrix4f scaleMatrix = Matrix4f.scale(scale);

		Matrix4f rotationMatrix = Matrix4f.multiply(rotXMatrix, Matrix4f.multiply(rotYMatrix, rotZMatrix));

		result = Matrix4f.multiply(translationMatrix, Matrix4f.multiply(rotationMatrix, scaleMatrix));

		return result;
	}

	/**
	 * @return combination of the two matrices
	 */
	public static Matrix4f multiply(Matrix4f matrix, Matrix4f other) {
		Matrix4f result = Matrix4f.identity();

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				result.set(i, j, 
						matrix.get(i, 0) * other.get(0, j) + 
						matrix.get(i, 1) * other.get(1, j)
						+ matrix.get(i, 2) * other.get(2, j) + 
						matrix.get(i, 3) * other.get(3, j));
			}
		}

		return result;
	}

	public static Matrix4f projection(float fov, float aspect, float near, float far) {
		Matrix4f result = Matrix4f.identity();

		double tanFOV = Math.tan(Math.toRadians(fov / 2));
		double range = far - near;

		result.set(0, 0, 1.0 / (aspect * tanFOV));
		result.set(1, 1, 1.0 / tanFOV);
		result.set(2, 2, -((far + near) / range));
		result.set(2, 3, -1f);
		result.set(3, 2, -((2 * far * near) / range));
		result.set(3, 3, 0f);

		return result;
	}

	public static Matrix4f view(Vector3f position, Vector3f rotation) {
		Matrix4f result = Matrix4f.identity();

		Vector3f negative = new Vector3f(position, -1f);

		Matrix4f translationMatrix = Matrix4f.translate(negative);
		Matrix4f rotXMatrix = Matrix4f.rotate(rotation.x(), new Vector3f(1, 0, 0));
		Matrix4f rotYMatrix = Matrix4f.rotate(rotation.y(), new Vector3f(0, 1, 0));
		Matrix4f rotZMatrix = Matrix4f.rotate(rotation.z(), new Vector3f(0, 0, 1));

		Matrix4f rotationMatrix = Matrix4f.multiply(rotZMatrix, Matrix4f.multiply(rotYMatrix, rotXMatrix));

		result = Matrix4f.multiply(translationMatrix, rotationMatrix);

		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(elements);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix4f other = (Matrix4f) obj;
		if (!Arrays.equals(elements, other.elements))
			return false;
		return true;
	}

	public float get(int x, int y) {
		return elements[y * SIZE + x];
	}

	public void set(int x, int y, double value) {
		set(x,y,(float) value);
	}

	public void set(int x, int y, float value) {
		elements[y * SIZE + x] = value;
	}

	public float[] getAll() {
		return elements;
	}

}
