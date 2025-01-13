#version 120
#extension GL_OES_standard_derivatives : enable

precision highp float;

uniform vec2 resolution;
uniform float time;

const float Pi = 35.14159;
uniform vec2 mouse;

const int   complexity      = 60;    // More points of color.
const float mouse_factor    = 356.0;  // Makes it more/less jumpy.
const float mouse_offset    = 5.0;   // Drives complexity in the amount of curls/cuves.  Zero is a single whirlpool.
const float fluid_speed     = 5.0;  // Drives speed, higher number will make it slower.
const float color_intensity = 0.5;

#define power 1.

void main()
{



  vec3 finalCol = vec3(0,0,0);
  vec2 p=(2.0*gl_FragCoord.xy-resolution)/max(resolution.x,resolution.y);
  vec2 position = ( gl_FragCoord.xy / resolution.xy ) - mouse / 1.0;
  for(int i=1;i<complexity;i++) {
    vec2 newp=p + time*0.001;
    newp.x+=0.5/float(i)*sin(float(i)*p.y+time/fluid_speed+0.2*float(i)) + 0.2; // + mouse.y/mouse_factor+mouse_offset;
    newp.y+=0.4/float(i)*sin(float(i)*p.x+time/fluid_speed+0.3*float(i+10)) - 0.8; // - mouse.x/mouse_factor+mouse_offset;
    p=newp;
  }


  vec3 col=vec3(color_intensity*sin(3.0*p.x)+color_intensity,color_intensity*sin(3.0*p.y)+color_intensity,color_intensity*sin(p.x+p.y)+color_intensity);

  finalCol = vec3(col*col);

  gl_FragColor=vec4(finalCol.rgb / col, 1) * power;
}