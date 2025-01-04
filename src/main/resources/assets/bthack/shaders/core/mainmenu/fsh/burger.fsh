#version 120
#ifdef GL_ES
precision mediump float;
#endif

// glslsandbox uniforms
uniform float time;
uniform vec2 resolution;
uniform sampler2D texture;

// shadertoy globals
float iTime = 9.8;
vec3  iResolution = vec3(5.13);

// Emulate a black texture7
#define texture(s, uv) vec4(0.0)

// --------[ Original ShaderToy begins here ]---------- //
/*
	"Cheeseburger" by Xor (@XorDev)
	License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
	This is a cheeseburger SDF I made in three days for a fun little challenge.
	The code isn't very organized because of time restraints. Sorry!
*/

//Anti-Aliasing level. Use 2, 3 or 4 if you have a better PC.
#define AA 1.
//Light direction vector.
#define DIR normalize(vec3(-2,4,3))


//Inspired by iq's smooth minimum function: http://www.iquilezles.org/www/articles/smin/smin.htm
float Smooth(float A, float B, float N)
{
 	return log(exp(A*N)+exp(B*N))/N;
}
//Generic 3D hash function.
vec3 Hash(vec3 P)
{
 	return fract(sin(P*mat3(45.32,32.42,-41.55,65.35,-43.42,51.55,45.32,29.82,-41.45)*vec3(142.83,253.36,188.64)));
}
//Smooth 3D Worley noise.
float Worley(vec3 P)
{
    float D = 1.;
    for(int X = -1;X<=1;X++)
    for(int Y = -1;Y<=1;Y++)
    for(int Z = -1;Z<=1;Z++)
    {
    	vec3 F = floor(P+vec3(X,Y,Z));
        vec3 V = P-F-Hash(F);
        D = Smooth(D,dot(V,V),-6.);
    }
    return D;
}
//Heavily modified Worley noise for the Sesame seeds.
float Seed(vec3 P)
{
    float D = 1.;
    for(int X = -1;X<=1;X++)
    for(int Y = -1;Y<=1;Y++)
    {
    	vec3 F = floor(vec3(P.xy,0)+vec3(X,Y,0));
        vec3 H = Hash(F)*vec3(1,1,63);
        vec3 V = mat3(cos(H.z),sin(H.z),0,sin(H.z),-cos(H.z),0,0,0,1)*(P-F-H*.9)*vec3(1.7,1,0);
        D = min(D,dot(V,V));
    }
    return smoothstep(.1*clamp(P.z-3.5,0.,1.),-.01,D);
}
//Cheese SDF.
float Cheese(vec3 P)
{
    P.z += .22+.1*Smooth(dot(P.xy,P.xy)-3.,0.,10.);
 	return (length(max(abs(P)-vec3(1.7,1.7,.01),0.))-.02)*.9;
}
//Burger, Plate and table SDF.
float Model(vec3 P)
{
    float D = length(P)-2.5;
    float M = Smooth(length(P.xy)-3.,pow(P.z-Smooth(D,0.,20.)*.7+1.1,2.)-.01,10.);
    M = min(M,Smooth(P.z+1.2,-abs(mod(P.y+7.,8.)-4.),12.));
    if (D<.1)
    {
    	vec3 B = vec3(P.xy,max(abs(P.z)-.5,0.)*1.8);
    	float Bun = max(length(B+.05*sin(B.yzx*2.))*.6-1.2,.5-abs(P.z+.1)-.02*B.x*B.x);
        M = min(M,Smooth(Bun,-1.-P.z,20.));
    	M = min(M,Smooth(length(P.xy+.1*sin(B.yx*2.))-2.1,pow(P.z+.03*P.x*P.x,2.)-.04,12.));
    	vec3 L = P-vec3(0,0,.26)+vec3(0,0,clamp(length(P.xy)-2.,.0,.2))*
        	cos(P.x*5.+sin(P.x*2.+P.y*4.))*sin(P.y*3.+P.x*3.+cos(P.y-P.x*4.));
    	M = min(M,Smooth(length(L)-2.3,abs(L.z)-.05,12.)*.8);
    	M = min(M,max(length(P)-1.9,abs(P.z+.45-.03*P.y*P.y)-.1));
    	M = min(M,Cheese(P));

        return M;
    }

	return min(D,M);
}
//Bump mapped distance function.
float Bump(vec3 P)
{
    float S = .02*Seed(P*5.);
    float B = .005*max(1.-1.5*abs(P.z),0.)*Worley(P*18.);
        B += .0004*Worley(P*40.)*step(-.99,P.z-max(length(P.xy)-2.,0.));
 	return Model(P)+B*step(.01,Cheese(P))-S;
}
//Typical Normal function.
vec3 Normal(vec3 P)
{
	vec3 N = vec3(-1,1,0)*.001;
    return normalize(Bump(P+N.xyy)*N.xyy+Bump(P+N.yxy)*N.yxy+Bump(P+N.yyx)*N.yyx+Bump(P+N.xxx)*N.xxx);
}
//Burger, plate and table texturing with lighting.
vec3 Tex(vec3 P,vec3 R)
{
    vec3 L = P-vec3(0,0,.26)+vec3(0,0,clamp(length(P.xy)-2.,.0,.2))*
        cos(P.x*5.+sin(P.x*2.+P.y*4.))*sin(P.y*3.+P.x*3.+cos(P.y-P.x*4.));
    float T = max(length(P)-1.9,abs(P.z+.45-.03*P.y*P.y)-.1);

    vec3 N = Normal(P);
    float W = Worley(P*11.)*(.05+.95*smoothstep(.7,.4,abs(P.z+.04)))*(abs(N.z)*.7+.3);


 	float M = abs(P.z+.03*P.x*P.x)-.3;
    float D = max(dot(N,DIR),.1)/(1.-min(dot(DIR,P),0.)*exp(4.-2.3*length(cross(DIR,P))));
    float S = max(dot(reflect(R,N),DIR),0.);

    vec3 B = vec3(.8,.55,.3)*(1.8-smoothstep(.0,1.2,pow(abs(P.z+.1),2.)))
        +.2*S*S-2.*W+vec3(.5+S*S*S)*Seed(P*5.);
    vec3 C = mix(B,vec3(.3,.18,.12)-.4*W+.9*S*S*S,step(M,.01));
    C = mix(C,vec3(.5,.7,.2)-.4*W+.5*S*S,step(abs(L.z),.06));
    C = mix(C,vec3(1,.6,.1)+S*S,step(Cheese(P),.01));
    C = mix(C,vec3(.8,.2,.1)+S*S,step(T,.01));
    C = mix(C,vec3(.9)+S*S*S*S,step(P.z-max(length(P.xy)-2.,0.),-.99));
    C = mix(C,(texture(iChannel0,P.xy/4.).rgb*vec3(.5,.6,.8)+.1),step(P.z,-1.18));

    return D*C;
}

//Output the results.
void mainImage( out vec4 Color, in vec2 Coord )
{
    vec2 A = vec2(iTime*.2,.2*cos(iTime*.1)+1.8);
    vec3 X = vec3(cos(A.x)*sin(A.y),sin(A.x)*sin(A.y),cos(A.y)),
         Y = normalize(cross(X,vec3(0,0,-1))),
    	 Z = normalize(cross(X,Y));

    mat3 M = mat3(X,Y,Z);

    vec3 C = vec3(0);

    for(float J = 0.;J<AA;J++)
    for(float K = 0.;K<AA;K++)
    {
        vec4 P = vec4(M*vec3(-10,0,2.*cos(A.y)+.4),0);
        vec3 R = M*vec3(1,(vec2(J,K)/AA+Coord-.5*iResolution.xy)/iResolution.x);

        for(int I = 0;I<200;I++)
        {
            float S = Model(P.xyz);
            P += vec4(R,1)*S;
            if ((P.w>30.) || (S<.001)) break;
        }

    	C += Tex(P.xyz,R)*max(1.-P.w*P.w/900.,0.);
    }
	C=C+0.4*C*(1.0+sin(time*8.0)*0.5);

    Color = vec4(pow(C/AA/AA, vec3(1.0 / 2.2)), 1);

}

void main(void)
{
    iTime = time;
    iResolution = vec3(resolution, 11.9);

    mainImage(gl_FragColor, gl_FragCoord.xy);
}