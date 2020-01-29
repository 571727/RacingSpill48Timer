precision mediump float;
in vec3 passColor;
in vec2 passTextureCoord;

out vec4 FragColor;

uniform sampler2D tex;

void main() {
	FragColor = mix(texture(tex, passTextureCoord).rgba, vec4(passColor, 1.0), 0.2); 
}