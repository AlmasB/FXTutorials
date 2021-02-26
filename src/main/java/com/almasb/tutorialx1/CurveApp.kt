package com.almasb.tutorialx1

import javafx.application.Application
import javafx.geometry.Point2D
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.Pane
import javafx.scene.shape.Circle
import javafx.stage.Stage
import java.util.*

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CurveApp : Application() {


    fun circ(point1: Point2D, point2: Point2D, point3: Point2D): Circle? {


        val ax = point1.x
        val ay = point1.y
        val bx = point2.x
        val by = point2.y
        val cx = point3.x
        val cy = point3.y

        var x = 0.0
        var y = 0.0

        //'''find the x,y and radius for the circle through the 3 points'''

        if (ax*by-ax*cy-cx*by+cy*bx-bx*ay+cx*ay != 0.0 ) {

            x = .5 * (-pow(ay, 2) * cy + pow(ay, 2) * by - ay * pow(bx, 2)
            -ay * pow(by, 2) + ay * pow(cy, 2) + ay * pow(cx, 2) -
            pow(cx, 2) * by + pow(ax, 2) * by + pow(bx, 2) *
            cy - pow(ax, 2) * cy - pow(cy, 2) * by + cy * pow(by, 2)) / (ax * by - ax * cy - cx * by + cy * bx - bx * ay + cx * ay)

            y = -.5 * (-pow(ax, 2) * cx + pow(ax, 2) * bx - ax * pow(by, 2)
            -ax * pow(bx, 2) + ax * pow(cx, 2) + ax * pow(cy, 2) -
            pow(cy, 2) * bx + pow(ay, 2) * bx + pow(by, 2) * cx
            -pow(ay, 2) * cx - pow(cx, 2) * bx + cx * pow(bx, 2)) / (ax * by - ax * cy - cx * by + cy * bx - bx * ay + cx * ay)

        } else {
            println("Fail")
            return null
        }

        val r = pow(pow(x-ax, 2.0)+pow(y-ay, 2.0), .5)

        return Circle(x, y, r)
    }

    fun findPoint(eq1: Circle, eq2: Circle, point1: Point2D, point2: Point2D): Point2D {

        //'''find the centroid of the overlapping part of two circles from their equations'''
        var thetabeg = Math.acos((point1.x-eq1.centerX)/eq1.radius)
        var thetaend = Math.acos((point2.x-eq1.centerX)/eq1.radius)
        val mid1x = eq1.radius*Math.cos((thetabeg+thetaend)/2)+eq1.centerX
        val thetaybeg = Math.asin((point1.y-eq1.centerY)/eq1.radius)
        val thetayend = Math.asin((point2.y-eq1.centerY)/eq1.radius)
        val mid1y = eq1.radius*Math.sin((thetaybeg+thetayend)/2)+eq1.centerY

        val thetabeg2 = Math.acos((point1.x-eq2.centerX)/eq2.radius)
        val thetaend2 = Math.acos((point2.x-eq2.centerX)/eq2.radius)
        val mid2x = eq2.radius*Math.cos((thetabeg2+thetaend2)/2)+eq2.centerX
        val thetaybeg2 = Math.asin((point1.y-eq2.centerY)/eq2.radius)
        val thetayend2 = Math.asin((point2.y-eq2.centerY)/eq2.radius)
        val mid2y = eq2.radius*Math.sin((thetaybeg2+thetayend2)/2)+eq2.centerY

        return Point2D((mid2x+mid1x)/2, (mid2y+mid1y)/2)
    }

    fun pow(x: Double, n: Double) = Math.pow(x, n)

    fun pow(x: Double, n: Int) = pow(x, n.toDouble())

    fun p(i: Int, i1: Int): Point2D {
        return Point2D(i.toDouble(), i1.toDouble())
    }

    fun draw(g: GraphicsContext) {
        val curve = mutableListOf<Point2D>(
                p(25,500),
                p(90,400),
                p(250,250),
                p(350,250),
                //p(300,276),
                p(362,215),
                p(400, 400),
                p(125, 500),
                p(25, 500)
                //,[445,122],[551,57],[560,23], [540, 10], [520,20],[500,50],[450,70]
        )

        //bplot(curve, blank)

        for (j in 0..6 - 1) {
            val newpoints = ArrayList<Point2D>()

            for (i in 0..curve.size - 3 - 1) {
                val eq = circ(curve[i], curve[i + 1], curve[i + 2])
                val eq2 = circ(curve[i + 1], curve[i + 2], curve[i + 3])

                if (eq == null || eq2 == null)
                    newpoints.add( Point2D((curve[i + 1].x + curve[i + 2].x) / 2, (curve[i+1].y+curve[i+2].y)/2 ))
                else
                    newpoints.add(findPoint(eq, eq2, curve[i + 1], curve[i + 2]))
            }

            for (i in newpoints.indices) {
                //newpoints[i] = Point2D(Math.r)

                //point[0] = int(round(point[0]))
                //point[1] = int(round(point[1]))
            }

            for (m in 0..newpoints.size - 1)
                curve.add(2 * m + 2, newpoints[m])
        }

        for (p in curve) {
            g.fillOval(p.x, p.y, 1.0, 1.0)
        }


        //plot(curve, blank)
    }

    fun draw2(g: GraphicsContext) {

        val points = mutableListOf<Point2D>(
                p(25,500),
                p(90,400),
                p(250,250),
                p(350,250),
                //p(300,276),
                p(362,215),
                p(400, 400),
                p(125, 500),
                p(25, 500)
                //,[445,122],[551,57],[560,23], [540, 10], [520,20],[500,50],[450,70]
        )

        g.beginPath()

        // move to the first point
        g.moveTo(points[0].x, points[0].y);


        for (i in 1..points.size - 3) {
            var xc = (points[i].x + points[i + 1].x) / 2;
            var yc = (points[i].y + points[i + 1].y) / 2;
            g.quadraticCurveTo(points[i].x, points[i].y, xc, yc);
        }

        val i = points.size - 2

        // curve through the last two points
        g.quadraticCurveTo(points[i].x, points[i].y, points[i+1].x,points[i+1].y);

        g.closePath()

        g.stroke()

        points.forEach {
            g.fillOval(it.x, it.y, 5.0, 5.0)
        }
    }









    /* pubic static function drawCurve
		 *	Draws a single cubic Bézier curve
		 *	@param:
		 *		g:Graphics			-Graphics on which to draw the curve
		 *		p1:Point			-First point in the curve
		 *		p2:Point			-Second point (control point) in the curve
		 *		p3:Point			-Third point (control point) in the curve
		 *		p4:Point			-Fourth point in the curve
		 *	@return:
		 */
    fun drawCurve(g:GraphicsContext, p1:Point2D, p2:Point2D, p3:Point2D, p4:Point2D) {
//        var bezier = new BezierSegment(p1,p2,p3,p4);	// BezierSegment using the four points
//        g.moveTo(p1.x,p1.y);
//        // Construct the curve out of 100 segments (adjust number for less/more detail)
//        for (var t=.01;t<1.01;t+=.01){
//            var val = bezier.getValue(t);	// x,y on the curve for a given t
//            g.lineTo(val.x,val.y);
//        }
    }

    // FROM http://www.cartogrammar.com/blog/actionscript-curves-update/
    /* public static function curveThroughPoints
     *	Draws a smooth curve through a series of points. For a closed curve, make the first and last points the same.
     *	@param:
     *		g:Graphics			-Graphics on which to draw the curve
     *		p:Array				-Array of Point instances
     *		z:Number			-A factor (between 0 and 1) to reduce the size of curves by limiting the distance of control points from anchor points.
     *							 For example, z=.5 limits control points to half the distance of the closer adjacent anchor point.
     *							 I put the option here, but I recommend sticking with .5
     *		angleFactor:Number	-Adjusts the size of curves depending on how acute the angle between points is. Curves are reduced as acuteness
     *							 increases, and this factor controls by how much.
     *							 1 = curves are reduced in direct proportion to acuteness
     *							 0 = curves are not reduced at all based on acuteness
     *							 in between = the reduction is basically a percentage of the full reduction
     *		moveTo:Bollean		-Specifies whether to move to the first point in the curve rather than continuing drawing
     * 							 from wherever drawing left off.
     *	@return:
     */
    fun curveThroughPoints(g:GraphicsContext, points:List<Point2D>/*of Points*/, z:Double = .5, angleFactor:Double = .75, moveTo:Boolean = true) {
//        try {
//            var p:Array = points.slice();	// Local copy of points array
//            var duplicates:Array = new Array();	// Array to hold indices of duplicate points
//            // Check to make sure array contains only Points
//            for (var i=0; i<p.length; i++){
//                if (!(p[i] is Point)){
//                    throw new Error("Array must contain Point objects");
//                }
//                // Check for the same point twice in a row
//                if (i > 0){
//                    if (p[i].x == p[i-1].x && p[i].y == p[i-1].y){
//                        duplicates.push(i);	// add index of duplicate to duplicates array
//                    }
//                }
//            }
//            // Loop through duplicates array and remove points from the points array
//            for (i=duplicates.length-1; i>=0; i--){
//                p.splice(duplicates[i],1);
//            }
//            // Make sure z is between 0 and 1 (too messy otherwise)
//            if (z <= 0){
//                z = .5;
//            } else if (z > 1){
//                z = 1;
//            }
//            // Make sure angleFactor is between 0 and 1
//            if (angleFactor < 0){
//                angleFactor = 0;
//            } else if (angleFactor > 1){
//                angleFactor = 1;
//            }
//
//            //
//            // First calculate all the curve control points
//            //
//
//            // None of this junk will do any good if there are only two points
//            if (p.length > 2){

        g.beginPath()




        // Ordinarily, curve calculations will start with the second point and go through the second-to-last point
        var firstPt = 1;
        var lastPt = points.size - 1;

        // Check if this is a closed line (the first and last points are the same)
        if (points[0].x == points[points.size-1].x && points[0].y == points[points.size-1].y){
            // Include first and last points in curve calculations
            firstPt = 0
            lastPt = points.size
        }

        val controlPts = ArrayList<Pair<Point2D, Point2D> >()   // An array to store the two control points (of a cubic Bézier curve) for each point
        for (i in 0..points.size - 1) {
            controlPts.add(Pair(Point2D.ZERO, Point2D.ZERO))
        }


        // Loop through all the points (except the first and last if not a closed line) to get curve control points for each.
        for (i in firstPt..lastPt - 1) {

            // The previous, current, and next points
            var p0 = if (i-1 < 0) points[points.size-2] else points[i-1];	// If the first point (of a closed line), use the second-to-last point as the previous point
            var p1 = points[i];
            var p2 = if (i+1 == points.size) points[1] else points[i+1];		// If the last point (of a closed line), use the second point as the next point

            var a = p0.distance(p1)	// Distance from previous point to current point
            if (a < 0.001) a = .001;		// Correct for near-zero distances, a cheap way to prevent division by zero
            var b = p1.distance(p2);	// Distance from current point to next point
            if (b < 0.001) b = .001;
            var c = p0.distance(p2);	// Distance from previous point to next point
            if (c < 0.001) c = .001;
            var cos = (b*b+a*a-c*c)/(2*b*a);

            // Make sure above value is between -1 and 1 so that Math.acos will work
            if (cos < -1)
                cos = -1.0;
            else if (cos > 1)
                cos = 1.0;

            var C = Math.acos(cos);	// Angle formed by the two sides of the triangle (described by the three points above) adjacent to the current point
            // Duplicate set of points. Start by giving previous and next points values RELATIVE to the current point.
            var aPt = Point2D(p0.x-p1.x,p0.y-p1.y);
            var bPt = Point2D(p1.x,p1.y);
            var cPt = Point2D(p2.x-p1.x,p2.y-p1.y);

            /*
            We'll be adding adding the vectors from the previous and next points to the current point,
            but we don't want differing magnitudes (i.e. line segment lengths) to affect the direction
            of the new vector. Therefore we make sure the segments we use, based on the duplicate points
            created above, are of equal length. The angle of the new vector will thus bisect angle C
            (defined above) and the perpendicular to this is nice for the line tangent to the curve.
            The curve control points will be along that tangent line.
            */

            if (a > b){
                //aPt.normalize(b);	// Scale the segment to aPt (bPt to aPt) to the size of b (bPt to cPt) if b is shorter.
                aPt = aPt.normalize().multiply(b)
            } else if (b > a){
                //cPt.normalize(a);	// Scale the segment to cPt (bPt to cPt) to the size of a (aPt to bPt) if a is shorter.
                cPt = cPt.normalize().multiply(a)
            }

            // Offset aPt and cPt by the current point to get them back to their absolute position.
            aPt = aPt.add(p1.x,p1.y);
            cPt = cPt.add(p1.x,p1.y);

            // Get the sum of the two vectors, which is perpendicular to the line along which our curve control points will lie.
            var ax = bPt.x-aPt.x;	// x component of the segment from previous to current point
            var ay = bPt.y-aPt.y;
            var bx = bPt.x-cPt.x;	// x component of the segment from next to current point
            var by = bPt.y-cPt.y;
            var rx = ax + bx;	// sum of x components
            var ry = ay + by;

            // Correct for three points in a line by finding the angle between just two of them
            if (rx == 0.0 && ry == 0.0){
                rx = -bx;	// Really not sure why this seems to have to be negative
                ry = by;
            }
            // Switch rx and ry when y or x difference is 0. This seems to prevent the angle from being perpendicular to what it should be.
            if (ay == 0.0 && by == 0.0){
                rx = 0.0;
                ry = 1.0;
            } else if (ax == 0.0 && bx == 0.0){
                rx = 1.0;
                ry = 0.0;
            }
            var r = Math.sqrt(rx*rx+ry*ry);	// length of the summed vector - not being used, but there it is anyway
            var theta = Math.atan2(ry,rx);	// angle of the new vector

            var controlDist = Math.min(a,b)*z;	// Distance of curve control points from current point: a fraction the length of the shorter adjacent triangle side
            var controlScaleFactor = C/Math.PI;	// Scale the distance based on the acuteness of the angle. Prevents big loops around long, sharp-angled triangles.

            controlDist *= ((1.0-angleFactor) + angleFactor*controlScaleFactor);	// Mess with this for some fine-tuning

            var controlAngle = theta+Math.PI/2;	// The angle from the current point to control points: the new vector angle plus 90 degrees (tangent to the curve).


            var controlPoint2 = polar(controlDist, controlAngle);	// Control point 2, curving to the next point.
            var controlPoint1 = polar(controlDist, controlAngle+Math.PI);	// Control point 1, curving from the previous point (180 degrees away from control point 2).

            // Offset control points to put them in the correct absolute position
            controlPoint1 = controlPoint1.add(p1.x,p1.y);
            controlPoint2 = controlPoint2.add(p1.x,p1.y);

            /*
            Haven't quite worked out how this happens, but some control points will be reversed.
            In this case controlPoint2 will be farther from the next point than controlPoint1 is.
            Check for that and switch them if it's true.
            */
            if (controlPoint2.distance(p2) > controlPoint1.distance(p2)){
                controlPts[i] = Pair(controlPoint2,controlPoint1);	// Add the two control points to the array in reverse order
            } else {
                controlPts[i] = Pair(controlPoint1,controlPoint2);	// Otherwise add the two control points to the array in normal order
            }

            // Uncomment to draw lines showing where the control points are.
            /*
            g.moveTo(p1.x,p1.y);
            g.lineTo(controlPoint2.x,controlPoint2.y);
            g.moveTo(p1.x,p1.y);
            g.lineTo(controlPoint1.x,controlPoint1.y);
            */
        }


        //
        // Now draw the curve
        //
        // If moveTo condition is false, this curve can connect to a previous curve on the same graphics.
        if (moveTo)
            g.moveTo(points[0].x, points[0].y);
        else
            g.lineTo(points[0].x, points[0].y);

        // If this isn't a closed line
        if (firstPt == 1){
            // Draw a regular quadratic Bézier curve from the first to second points, using the first control point of the second point
            g.quadraticCurveTo(controlPts[1].first.x, controlPts[1].first.y, points[1].x, points[1].y);
        }

        var straightLines:Boolean = true;	// Change to true if you want to use lineTo for straight lines of 3 or more points rather than curves. You'll get straight lines but possible sharp corners!

        // Loop through points to draw cubic Bézier curves through the penultimate point, or through the last point if the line is closed.
        for (i in firstPt..lastPt - 2){
            // Determine if multiple points in a row are in a straight line
            var isStraight:Boolean = ( ( i > 0 && Math.atan2(points[i].y-points[i-1].y, points[i].x-points[i-1].x)
                    == Math.atan2(points[i+1].y-points[i].y, points[i+1].x - points[i].x) )
                    || ( i < points.size - 2 && Math.atan2(points[i+2].y-points[i+1].y,points[i+2].x-points[i+1].x)
                    == Math.atan2(points[i+1].y-points[i].y,points[i+1].x-points[i].x) ) );

            if (straightLines && isStraight){
                g.lineTo(points[i+1].x,points[i+1].y);
            } else {



                // BezierSegment instance using the current point, its second control point, the next point's first control point, and the next point
                //var bezier:BezierSegment = new BezierSegment(points[i], controlPts[i].second, controlPts[i+1].first, points[i+1]);

                // Construct the curve out of 100 segments (adjust number for less/more detail)

                var t = 0.01
                while (t < 1.01) {

                    val point = getBezierValue(points[i], controlPts[i].second, controlPts[i+1].first, points[i+1], t)
                    g.lineTo(point.x, point.y)

                    t += 0.01
                }

//                for (var t=.01;t<1.01;t+=.01){
//                    var v = bezier.getValue(t);	// x,y on the curve for a given t
//                    g.lineTo(v.x,v.y);
//                }
            }
        }

//        // If this isn't a closed line
//        if (lastPt == points.size-1){
//            // Curve to the last point using the second control point of the penultimate point.
//            g.quadraticCurveTo(controlPts[i].second.x, controlPts[i].second.y, points[i+1].x, points[i+1].y);
//        }


        g.closePath()
        g.stroke()


//                // just draw a line if only two points
//            } else if (p.length == 2){
//                g.moveTo(p[0].x,p[0].y);
//                g.lineTo(p[1].x,p[1].y);
//            }


//        }
//        // Catch error
//        catch (e) {
//            trace(e.getStackTrace());
//        }
    }

    fun polar(len: Double, angle: Double): Point2D {
        return Point2D(len * Math.cos(angle), len * Math.sin(angle))
    }

    fun getBezierValue(p1: Point2D, p2: Point2D, p3: Point2D, p4: Point2D, t: Double): Point2D {
        val x = Math.pow(1 - t, 3.0) * p1.x + 3 * t * Math.pow(1 - t, 2.0) * p2.x + 3 * t*t * (1 - t) * p3.x + t*t*t*p4.x
        val y = Math.pow(1 - t, 3.0) * p1.y + 3 * t * Math.pow(1 - t, 2.0) * p2.y + 3 * t*t * (1 - t) * p3.y + t*t*t*p4.y
        return Point2D(x, y)
    }

























    fun createContent(): Parent {

        val pane = Pane()
        val canvas = Canvas(600.0, 600.0)

        val g = canvas.graphicsContext2D

        pane.children.add(canvas)

        //draw2(g)

        val points = mutableListOf<Point2D>(
                p(25,500),
                p(90,400),
                p(250,250),
                p(350,250),
                //p(300,276),
                p(362,215),
                p(400, 400),
                p(125, 500),
                p(25, 500)
                //,[445,122],[551,57],[560,23], [540, 10], [520,20],[500,50],[450,70]
        )

        curveThroughPoints(g, points)

        points.forEach {
            g.fillOval(it.x, it.y, 5.0, 5.0)
        }

        return pane
    }

    override fun start(stage: Stage) {
        stage.scene = Scene(createContent())
        stage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(CurveApp::class.java, *args)
}
