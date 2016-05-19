#version 330 core

// Default Sprite vertex shader

layout (location = 0) out vec4 color;

in DATA{
	vec4 color;
	vec2 tc;
} fs_in;

uniform sampler2D tex;

void main() {
	color = texture(tex, fs_in.tc * 0.001); 
	if(color.a == 0) 
	{
		discard;
	} 
	color.a -= fs_in.color.a;
}