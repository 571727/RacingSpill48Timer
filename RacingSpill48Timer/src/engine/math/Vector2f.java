package engine.math;

public class Vector2f {

	private float[] xy;

	public Vector2f() {
		this(0,0);
	}
	
	public Vector2f(Vector2f vec2, float modifier) {
		xy = new float[2];
		for (int i = 0; i < 2; i++) {
			xy[i] = modifier * vec2.get(i);
		}
	}

	public Vector2f(float xy) {
		this(xy, xy);
	}

	public Vector2f(double x, double y) {
		this((float) x, (float) y);
	}

	public Vector2f(float x, float y) {
		xy = new float[2];
		xy[0] = x;
		xy[1] = y;
	}

	public static Vector2f add(Vector2f v1, Vector2f v2) {
		return new Vector2f(v1.x() + v2.x(), v1.y() + v2.y());
	}

	public static Vector2f sub(Vector2f v1, Vector2f v2) {
		return new Vector2f(v1.x() - v2.x(), v1.y() - v2.y());
	}

	public static Vector2f mul(Vector2f v1, Vector2f v2) {
		return new Vector2f(v1.x() * v2.x(), v1.y() * v2.y());
	}

	public static Vector2f div(Vector2f v1, Vector2f v2) {
		return new Vector2f(v1.x() / v2.x(), v1.y() / v2.y());
	}

	/**
	 * pytagoras for vec3
	 */
	public static float length(Vector2f v) {
		return (float) Math.sqrt(v.x() * v.x() + v.y() * v.y());
	}

	/**
	 * Makes a vector the length of 1
	 */
	public static Vector2f normalize(Vector2f v) {
		float len = Vector2f.length(v);
		return Vector2f.div(v, new Vector2f(len));
	}

	/**
	 * How much is a vector pointing with another vector If it's positive it's
	 * pointing similarly to the other vector If it's 0 it's pointing
	 * perpendicularly If it's negative it's pointing away from the other vector
	 */
	public static float dot(Vector2f v1, Vector2f v2) {
		return v1.x() * v2.x() + v1.y() * v2.y();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for (float f : xy) {
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
		Vector2f other = (Vector2f) obj;
		for(int i = 0; i < 2; i++) {
			if(Float.floatToIntBits(xy[i]) != Float.floatToIntBits(other.get(i)));
		}
		
		return true;
	}
	
	
	/*
	 * Getters and setters
	 */

	public float get(int index) {
		return xy[index];
	}

	public void set(int index, float value) {
		xy[index] = value;
	}

	public float x() {
		return xy[0];
	}

	public void setX(float x) {
		this.xy[0] = x;
	}

	public float y() {
		return xy[1];
	}

	public void setY(float y) {
		this.xy[1] = y;
	}

}
