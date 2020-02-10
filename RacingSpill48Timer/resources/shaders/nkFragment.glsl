precision mediump float;

uniform sampler2D tex;

in vec2 passTextureCoord;
in vec4 passColor;

out vec4 FragColor;

void main(){
   FragColor = passColor * texture(tex, passTextureCoord.st);
}