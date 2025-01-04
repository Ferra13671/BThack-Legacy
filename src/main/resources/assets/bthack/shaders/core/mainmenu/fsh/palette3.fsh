#version 120
#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 resolution;
uniform float time;
uniform float u_value;
uniform sampler2D u_img_tex; // Use this for Image texture
uniform sampler2D u_lay_tex; // A layer texture
uniform sampler2D texture;
uniform vec2 mouse;

const float Pi = 100.;

const int   complexity      = 30;    // More points of color.
const float fluid_speed     = 10.0;  // Drives speed, higher number will make it slower.
const float color_intensity = 0.5;
void main(){
    vec2 p=(2.0*gl_FragCoord.xy-resolution)/max(resolution.x, resolution.y);

    for (int i=1;i<complexity;i++)  {
       vec2 newp=p + time*0.001;
        newp.x+=0.6/float(i)*sin(float(i)*p.y+time/fluid_speed+20.3*float(i) + mouse.x) + 0.5;// + mouse.y/mouse_factor+mouse_offset;
        newp.y+=0.6/float(i)*sin(float(i)*p.x+time/fluid_speed+0.3*float(i+10) + mouse.y) - 0.5;// - mouse.x/mouse_factor+mouse_offset;
        p=newp;
    }
    vec3 col=vec3(color_intensity*sin(5.0*p.x)+color_intensity, color_intensity*sin(3.0*p.y)+color_intensity, color_intensity*sin(p.x+p.y)+color_intensity);
    gl_FragColor=vec4(col, 1);
}