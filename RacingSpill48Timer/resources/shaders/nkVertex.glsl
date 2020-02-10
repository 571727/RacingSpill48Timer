uniform mat4 ProjMtx;

in vec2 Position;
in vec2 TexCoord;
in vec4 Color;

out vec2 passTextureCoord;
out vec4 passColor;

void main() {
   passTextureCoord = TexCoord;
   passColor = Color;
   gl_Position = ProjMtx * vec4(Position.xy, 0, 1);
}