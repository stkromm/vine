#version 330 core

// Default Sprite fragment shader

layout	(location = 0) in vec4 position;
layout (location = 1) in vec2 tc;
layout (location = 2) in vec4 color;

uniform mat4 pr_matrix;
uniform mat4 vw_matrix;

out DATA{
	vec4 color;
	vec2 tc;
} vs_out;

void main()
{
	gl_Position = pr_matrix * vw_matrix * position;
	vs_out.tc = tc;
	vs_out.color = color;
}