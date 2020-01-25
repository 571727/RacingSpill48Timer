package engine.math;

public class Vector3f {

	private float[] xyz;

	public Vector3f(Vector3f vec3, float modifier) {
		xyz = new float[3];
		for (int i = 0; i < 3; i++) {
			xyz[i] = modifier * vec3.get(i);
		}
	}

	public Vector3f(float xyz) {
		this(xyz, xyz, xyz);
	}

	public Vector3f(double x, double y, double z) {
		this((float) x, (float) y, (float) z);
	}

	public Vector3f(float x, float y, float z) {
		xyz = new float[3];
		xyz[0] = x;
		xyz[1] = y;
		xyz[2] = z;
	}

	public static Vector3f add(Vector3f v1, Vector3f v2) {
		return new Vector3f(v1.x() + v2.x(), v1.y() + v2.y(), v1.z() + v2.z());
	}

	public static Vector3f addX(Vector3f v1, float value) {
		return new Vector3f(v1.x() + value, v1.y(), v1.z());
	}

	public static Vector3f addY(Vector3f v1, float value) {
		return new Vector3f(v1.x(), v1.y() + value, v1.z());
	}

	public static Vector3f addZ(Vector3f v1, float value) {
		return new Vector3f(v1.x(), v1.y(), v1.z() + value);
	}

	public static Vector3f sub(Vector3f v1, Vector3f v2) {
		return new Vector3f(v1.x() - v2.x(), v1.y() - v2.y(), v1.z() - v2.z());
	}

	public static Vector3f subX(Vector3f v1, float value) {
		return addX(v1, -value);
	}

	public static Vector3f subY(Vector3f v1, float value) {
		return addY(v1, -value);
	}

	public static Vector3f subZ(Vector3f v1, float value) {
		return addZ(v1, -value);
	}

	public static Vector3f mul(Vector3f v1, Vector3f v2) {
		return new Vector3f(v1.x() * v2.x(), v1.y() * v2.y(), v1.z() * v2.z());
	}

	public static Vector3f mulX(Vector3f v1, float value) {
		return new Vector3f(v1.x() * value, v1.y(), v1.z());
	}

	public static Vector3f mulY(Vector3f v1, float value) {
		return new Vector3f(v1.x(), v1.y() * value, v1.z());
	}

	public static Vector3f mulZ(Vector3f v1, float value) {
		return new Vector3f(v1.x(), v1.y(), v1.z() * value);
	}

	public static Vector3f div(Vector3f v1, Vector3f v2) {
		return new Vector3f(v1.x() / v2.x(), v1.y() / v2.y(), v1.z() / v2.z());
	}

	public static Vector3f divX(Vector3f v1, float value) {
		return new Vector3f(v1.x() / value, v1.y(), v1.z());
	}

	public static Vector3f divY(Vector3f v1, float value) {
		return new Vector3f(v1.x(), v1.y() / value, v1.z());
	}

	public static Vector3f divZ(Vector3f v1, float value) {
		return new Vector3f(v1.x(), v1.y(), v1.z() / value);
	}

	/**
	 * pytagoras for vec3
	 */
	public static float length(Vector3f v) {
		return (float) Math.sqrt(v.x() * v.x() + v.y() * v.y() + v.z() * v.z());
	}

	/**
	 * Makes a vector the length of 1
	 */
	public static Vector3f normalize(Vector3f v) {
		float len = Vector3f.length(v);
		return Vector3f.div(v, new Vector3f(len));
	}

	/**
	 * How much is a vector pointing with another vector If it's positive it's
	 * pointing similarly to the other vector If it's 0 it's pointing
	 * perpendicularly If it's negative it's pointing away from the other vector
	 */
	public static float dot(Vector3f v1, Vector3f v2) {
		return v1.x() * v2.x() + v1.y() * v2.y() + v1.z() * v2.z();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for (float f : xyz) {
			result = prime * result + Float.floatToIntBits(f);
		}
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
		Vector3f other = (Vector3f) obj;
		for (int i = 0; i < 3; i++) {
			if (Float.floatToIntBits(xyz[i]) != Float.floatToIntBits(other.get(i)))
				;
		}

		return true;
	}

	/*
	 * Getters and setters
	 */

	public float get(int index) {
		return xyz[index];
	}

	public void set(int index, float value) {
		xyz[index] = value;
	}

	public float x() {
		return xyz[0];
	}

	public void setX(float x) {
		this.xyz[0] = x;
	}

	public float y() {
		return xyz[1];
	}

	public void setY(float y) {
		this.xyz[1] = y;
	}

	public float z() {
		return xyz[2];
	}

	public void setZ(float z) {
		this.xyz[2] = z;
	}

}
