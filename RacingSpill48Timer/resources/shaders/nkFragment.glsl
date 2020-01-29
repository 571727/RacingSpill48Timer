precision mediump float;

in vec2 passTextureCoord;
in vec4 passColor;

out vec4 FragColor;

uniform sampler2D tex;

void main(){
   FragColor = passColor * texture(tex, passTextureCoord.st);
}