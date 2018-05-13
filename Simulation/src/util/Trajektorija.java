package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class Trajektorija {
	
	
	public static void main(String arg[]) {
		Vector<Double> proj = new Vector<Double>(3);
		proj.add(0.0);
		proj.add(0.0);
		proj.add(0.0);
		
		Random r = new Random();
		
		int Low = -40;
		int High = 50;
		
		int count = 0;
		File logFile = new File("podaci.txt");
		File logFile1 = new File("rezultati.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
			BufferedWriter writer1 = new BufferedWriter(new FileWriter(logFile1));
			for (int i=0; i<3000000; i++) {
				
				
				Vector<Double> targ = new Vector<Double>(3);
				double x=r.nextDouble()*(High-Low) + Low;
				double y=r.nextDouble()*(High-Low) + Low;
				double z=r.nextDouble()*(High-0) + 0;
				targ.add( x);
				targ.add( y);
				targ.add (z);
				Vector<Double> targ_speed = new Vector<Double>(3);
				int High1 = 5;
				int Low1 = 0;
				double speed1 = r.nextDouble()*(High1-Low1) + Low1;
				double speed2 = r.nextDouble()*(High1-Low1) + Low1;
				double speed3 = r.nextDouble()*(High1-Low1) + Low1;
				targ_speed.add(speed1);
				targ_speed.add(speed2);
				targ_speed.add(speed3);
				
				float speed = 20.0f;
				Vector<Double>[] results = solve_ballistic_arc(proj, speed , targ, targ_speed, 9.81f );
				if (!results[0].isEmpty()) {
					count++;
					System.out.println( "For center x: " + x  + " and speed: "+speed1+", fire at + x1: "+ results[0].elementAt(0) );
					System.out.println( "For center y: " + y  + " and speed: "+speed2+", fire at + y1: "+ results[0].elementAt(1) );
					System.out.println( "For center z: " + z  + " and speed: "+speed3+", fire at + z1: "+ results[0].elementAt(2) );
					System.out.println("-----------------------------------------------------------------------------------------");
					String upisi = (float) x + "," +(float) y + "," +(float) z + "," + (float)speed1 + "," +(float) speed2 + "," + (float) speed3;
					writer.write(upisi);
					writer.newLine();
					String upisi1 =  results[0].elementAt(0) + "," + results[0].elementAt(1) + "," + results[0].elementAt(2);
					writer1.write(upisi1);
					writer1.newLine();
				}
	
				targ.clear();
				targ_speed.clear();
			}
			writer.close();
			writer1.close();
			System.out.println(count);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
    public static  boolean IsZero(double d) {
        double eps = Math.pow(Math.E,-9);
        return d > -eps && d < eps;
    }

    public static  int SolveQuadric(double c0, double c1, double c2,  Double s0,  Double s1, Double[] niz) {
        s0 = 0.0;
        s1 = 0.0;

        double p, q, D;

        /* normal form: x^2 + px + q = 0 */
        p = c1 / (2 * c0);
        q = c2 / c0;

        D = p * p - q;

        if (IsZero(D)) {
	        s0 = -p;
	        niz[0] = s0;
	        niz[1] = s1;
	        return 1;
        }
        else if (D < 0) {
        	niz[0] = s0;
	        niz[1] = s1;
	        return 0;
        }
        else /* if (D > 0) */ {
	        double sqrt_D = Math.sqrt(D);

	        s0 =   sqrt_D - p;
	        s1 = -sqrt_D - p;
	        niz[0] = s0;
	        niz[1] = s1;
	        return 2;
        }
    }

    public static  int SolveCubic(double c0, double c1, double c2, double c3,  Double s0,  Double s1,  Double s2 , Double[] niz)
    {
    /*    s0 = new Double(0);
        s1 = new Double(0);
        s2 = new Double(0);  */
    	s0 = 0.0;
    	s1 = 0.0;
    	s2 = 0.0;

        int     num;
        double  sub;
        double  A, B, C;
        double  sq_A, p, q;
        double  cb_p, D;

        A = c1 / c0;
        B = c2 / c0;
        C = c3 / c0;

        sq_A = A * A;
        p = 1.0/3 * (- 1.0/3 * sq_A + B);
        q = 1.0/2 * (2.0/27 * A * sq_A - 1.0/3 * A * B + C);


        cb_p = p * p * p;
        D = q * q + cb_p;

        if (IsZero(D)) {
	        if (IsZero(q))  {
	            s0 = 0.0;
	            num = 1;
	        }
	        else  {
	            double u = Math.pow(-q, 1.0/3.0);
	            s0 = 2 * u;
	            s1 = - u;
	            num = 2;
	        }
        }
        else if (D < 0)  {
	        double phi = 1.0/3 * Math.acos(-q / Math.sqrt(-cb_p));
	        double t = 2 * Math.sqrt(-p);

	        s0 =   t * Math.cos(phi);
	        s1 = - t * Math.cos(phi + Math.PI / 3);
	        s2 = - t * Math.cos(phi - Math.PI / 3);
	        num = 3;
        }
        else  {
	        double sqrt_D = Math.sqrt(D);
	        double u = Math.pow(sqrt_D - q, 1.0/3.0);
	        double v = - Math.pow(sqrt_D + q, 1.0/3.0);

	        s0 = u + v;
	        num = 1;
        }

        sub = 1.0/3 * A;

        if (num > 0)    s0 -= sub;
        if (num > 1)    s1 -= sub;
        if (num > 2)    s2 -= sub;
        
        
        niz[0] = s0;
        niz[1] = s1;
        niz[2] = s2;
        return num;
    }

    
    public static int SolveQuartic(double c0, double c1, double c2, double c3, double c4,  Double s0,  Double s1,  Double s2,  Double s3, Double[] ar) {
        s0 = new Double(0);
        s1 = new Double(0);
        s2 = new Double(0);
        s3 = new Double(0);

        double[]  coeffs = new double[4];
        double  z, u, v, sub;
        double  A, B, C, D;
        double  sq_A, p, q, r;
        int     num;
        Double[] niz = new Double[3];

        A = c1 / c0;
        B = c2 / c0;
        C = c3 / c0;
        D = c4 / c0;

        sq_A = A * A;
        p = - 3.0/8 * sq_A + B;
        q = 1.0/8 * sq_A * A - 1.0/2 * A * B + C;
        r = - 3.0/256*sq_A*sq_A + 1.0/16*sq_A*B - 1.0/4*A*C + D;

        if (IsZero(r)) {

	        coeffs[ 3 ] = q;
	        coeffs[ 2 ] = p;
	        coeffs[ 1 ] = 0;
	        coeffs[ 0 ] = 1;

	        num = SolveCubic(coeffs[0], coeffs[1], coeffs[2], coeffs[3],  s0,  s1,  s2, niz);
	        s0 = niz[0];
	        s1 = niz[1];
	        s2 = niz[2];
        }
        else {

	        coeffs[ 3 ] = 1.0/2 * r * p - 1.0/8 * q * q;
	        coeffs[ 2 ] = - r;
	        coeffs[ 1 ] = - 1.0/2 * p;
	        coeffs[ 0 ] = 1;
	        

            SolveCubic(coeffs[0], coeffs[1], coeffs[2], coeffs[3],  s0,  s1,  s2 , niz);
            s0 = niz[0];
	        s1 = niz[1];
	        s2 = niz[2];
	 
	        z = s0;


	        u = z * z - r;
	        v = 2 * z - p;

	        if (IsZero(u))
	            u = 0;
	        else if (u > 0)
	            u = Math.sqrt(u);
	        else {
	            ar[0] = s0;
	            ar[1] = s1;
	            ar[2] = s2;
	            ar[3] = s3;
	            return 0;
	        }

	        if (IsZero(v))
	            v = 0;
	        else if (v > 0)
	            v = Math.sqrt(v);
	        else {
	            ar[0] = s0;
	            ar[1] = s1;
	            ar[2] = s2;
	            ar[3] = s3;
	            return 0;
	        }

	        coeffs[ 2 ] = z - u;
	        coeffs[ 1 ] = q < 0 ? -v : v;
	        coeffs[ 0 ] = 1;

	        num = SolveQuadric(coeffs[0], coeffs[1], coeffs[2], s0,  s1 , niz);
	        
	        s0 = niz[0]; s1 = niz[1];

	        coeffs[ 2 ]= z + u;
	        coeffs[ 1 ] = q < 0 ? v : -v;
	        coeffs[ 0 ] = 1;

            if (num == 0) {num += SolveQuadric(coeffs[0], coeffs[1], coeffs[2],  s0,  s1 , niz); s0 = niz[0]; s1 = niz[1]; }
            if (num == 1) {num += SolveQuadric(coeffs[0], coeffs[1], coeffs[2],  s1,  s2 , niz); s0 = niz[0]; s1 = niz[1]; }
            if (num == 2) { num += SolveQuadric(coeffs[0], coeffs[1], coeffs[2],  s2,  s3, niz); s0 = niz[0]; s1 = niz[1]; }
        }

  
        sub = 1.0/4 * A;

        if (num > 0)    s0 -= sub;
        if (num > 1)    s1 -= sub;
        if (num > 2)    s2 -= sub;
        if (num > 3)    s3 -= sub;

        ar[0] = s0;
        ar[1] = s1;
        ar[2] = s2;
        ar[3] = s3;
        return num;
    }
    
    public  double ballistic_range(float speed, float gravity, float initial_height) {

    	double angle = Math.toRadians(45.0);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double range = (speed*cos/gravity) * (speed*sin + Math.sqrt(speed*speed*sin*sin + 2*gravity*initial_height));
        return range;
        
    }
    
    public static Vector<Double>[] solve_ballistic_arc(Vector<Double> proj_pos, float proj_speed, Vector<Double> target_pos, Vector<Double> target_velocity, float gravity) {


        double G = gravity;

        double A = (double) proj_pos.elementAt(0);
        double B = (double) proj_pos.elementAt(1);
        double C = (double) proj_pos.elementAt(2);
        double M = (double) target_pos.elementAt(0);
        double N = (double) target_pos.elementAt(1);
        double O = (double) target_pos.elementAt(2);
        double P = (double) target_velocity.elementAt(0);
        double Q = (double) target_velocity.elementAt(1);
        double R = (double) target_velocity.elementAt(2);
        double S = proj_speed;

        double H = M - A;
        double J = O - C;
        double K = N - B;
        double L = -.5f * G;

        double c0 = L*L;
        double c1 = 2*Q*L;
        double c2 = Q*Q + 2*K*L - S*S + P*P + R*R;
        double c3 = 2*K*Q + 2*H*P + 2*J*R;
        double c4 = K*K + H*H + J*J;

        double[] times = new double[4];
        Double[] niz = new Double[4];
        int numTimes = SolveQuartic(c0, c1, c2, c3, c4,  times[0],  times[1],  times[2],  times[3], niz);
        times[0] = niz[0];
        times[1] = niz[1];
        times[2] = niz[2];
        times[3] = niz[3];

        Arrays.sort(times);

        Vector<Double>[] solutions = (Vector<Double>[]) new Vector[2];
        
        Vector<Double> solution1 = new Vector<Double>(3);
        Vector<Double> solution2 = new Vector<Double>(3);
    
        int numSolutions = 0;

        for (int i = 0; i < numTimes && numSolutions < 2; ++i) {
            double t = times[i];
            if ( t <= 0)
                continue;
            if (numSolutions==0) {
            	solution1.add( ((H+P*t)/t) );
            	solution1.add((K+Q*t-L*t*t)/ t);
            	solution1.add(((J+R*t)/t));
            	++numSolutions;
            }else {
            	solution2.add( ((H+P*t)/t) );
            	solution2.add((K+Q*t-L*t*t)/ t);
            	solution2.add(((J+R*t)/t));
            	++numSolutions;
            }
        }
        
        solutions[0] = solution1;
        solutions[1] = solution2;

        return solutions;
    }   
    
}
