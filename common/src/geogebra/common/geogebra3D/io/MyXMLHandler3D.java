package geogebra.common.geogebra3D.io;

import geogebra.common.awt.GColor;
import geogebra.common.euclidian3D.EuclidianView3DInterface;
import geogebra.common.geogebra3D.kernel3D.geos.GeoPoint3D;
import geogebra.common.geogebra3D.kernel3D.geos.GeoQuadric3D;
import geogebra.common.geogebra3D.main.settings.EuclidianSettingsForPlane;
import geogebra.common.io.MyXMLHandler;
import geogebra.common.kernel.Construction;
import geogebra.common.kernel.Kernel;
import geogebra.common.kernel.geos.GeoPoint;
import geogebra.common.kernel.kernelND.GeoLevelOfDetail;
import geogebra.common.kernel.kernelND.GeoPlaneND;
import geogebra.common.kernel.kernelND.GeoPointND;
import geogebra.common.main.App;
import geogebra.common.main.settings.EuclidianSettings;
import geogebra.common.main.settings.EuclidianSettings3D;
import geogebra.common.util.StringUtil;

import java.util.LinkedHashMap;



/**
 * Class extending MyXMLHandler for 3D 
 * 
 * @author ggb3D
 * 
 *
 */
public class MyXMLHandler3D extends MyXMLHandler {

	/** See Kernel3D for using the constructor
	 * @param kernel
	 * @param cons
	 */
	public MyXMLHandler3D(Kernel kernel, Construction cons) {
		super(kernel, cons);
	}	
	
	// ====================================
	// <euclidianView3D> only used in 3D
	// ====================================
	/** only used in MyXMLHandler3D
	 * @param eName
	 * @param attrs
	 */
	@Override
	protected void startEuclidianView3DElement(String eName, LinkedHashMap<String, String> attrs) {
		

		// must do this first
		if (evSet == null)
			evSet = app.getSettings().getEuclidian(3);

		// make sure eg is reset the first time (for each EV) we get the
		// settings
		// "viewNumber" not stored for EV1 so we need to do this here
		if (resetEVsettingsNeeded) {
			resetEVsettingsNeeded = false;
			evSet.reset();
		}
		
		boolean ok = true;
		EuclidianView3DInterface ev = app.getEuclidianView3D();

		switch (eName.charAt(0)) {
		
		case 'a':
			if (eName.equals("axesColor")) {
				//ok = handleAxesColor(ev, attrs);
				break;
			} else if (eName.equals("axis")) {
				ok = handleAxis(evSet, attrs);
				break;
			}

			
		case 'b':
			if (eName.equals("bgColor")) {
				ok = handleBgColor(evSet, attrs);
				break;
			}
			

		case 'c':
			if (eName.equals("coordSystem")) {
				ok = handleCoordSystem3D((EuclidianSettings3D) evSet, attrs);
				break;
			}else if (eName.equals("clipping")) {
				ok = handleClipping((EuclidianSettings3D) evSet, attrs);
				break;
			}
			
		case 'e':
			if ("evSettings".equals(eName)) {
				ok = handleEvSettings(evSet, attrs);
				break;
			}

		case 'g':
			if (eName.equals("grid")) {
				ok = handleGrid(evSet, attrs);
				break;
			} 
			/*
			else if (eName.equals("gridColor")) {
				ok = handleGridColor(ev, attrs);
				break;
			}
			 */
			
		case 'p':
			if (eName.equals("plate")) {
				ok = handlePlate(ev, attrs);
				break;
			} else if (eName.equals("plane")) {
				ok = handlePlane(ev, attrs);
				break;
			} else if (eName.equals("projection")) {
				ok = handleProjection(ev, attrs);
				break;
			}
			
		case 'y':
			if (eName.equals("yAxisVertical")) {
				ok = handleYAxisIsUp(ev, attrs);
				break;
			}
			
			/*

		case 's':
			if (eName.equals("size")) {
				ok = handleEvSize(ev, attrs);
				break;
			}
			*/

		default:
			System.err.println("unknown tag in <euclidianView3D>: " + eName);
		}

		if (!ok) {
			System.err.println("error in <euclidianView3D>: " + eName);
		}
	}
	
	@Override
	protected void startGeoElement(String eName, LinkedHashMap<String, String> attrs) {
		if (geo == null) {
			App.debug("no element set for <" + eName + ">");
			return;
		}

		boolean ok = true;
		switch (eName.charAt(0)) {
		case 'f':
			if (eName.equals("fading")) {
				ok = handleFading(attrs);
				break;
			}
		case 'l':
			if (eName.equals("levelOfDetail")) {
				ok = handleLevelOfDetail(attrs);
				break;
			}

		default:
			super.startGeoElement(eName, attrs);
		}

		if (!ok) {
			App.debug("error in <element>: " + eName);
		}
	}
	
	private static boolean handleCoordSystem3D(EuclidianSettings3D evs, LinkedHashMap<String, String> attrs) {
		try {
			double xZero = Double.parseDouble(attrs.get("xZero"));
			double yZero = Double.parseDouble(attrs.get("yZero"));
			double zZero = Double.parseDouble(attrs.get("zZero"));
			
			double scale = Double.parseDouble(attrs.get("scale"));
			// TODO yScale, zScale

			double xAngle = Double.parseDouble(attrs.get("xAngle"));
			double zAngle = Double.parseDouble(attrs.get("zAngle"));
			

			evs.setXscale(scale);
			evs.setYscale(scale);
			evs.setZscale(scale);
			evs.setRotXYinDegrees(zAngle, xAngle);
			evs.updateOrigin(xZero,yZero,zZero);
			
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean handleFading(LinkedHashMap<String, String> attrs) {
		try {
			float fading = Float.parseFloat(attrs.get("val"));			
			((GeoPlaneND) geo).setFading(fading);			
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean handleLevelOfDetail(LinkedHashMap<String, String> attrs) {
		try {
			int lod = Integer.parseInt(attrs.get("val"));			
			((GeoLevelOfDetail) geo).getLevelOfDetail().setValue(lod);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	


	/** handles plane attributes for EuclidianView3D
	 * @param ev
	 * @param attrs
	 * @return true if all is done ok
	 * @deprecated
	 */
	protected boolean handlePlane(EuclidianView3DInterface ev, LinkedHashMap<String, String> attrs) {
		
		return handlePlate(ev, attrs);
	}
	
	/** handles plane attributes (show plate) for EuclidianView3D
	 * @param ev
	 * @param attrs
	 * @return true if all is done ok
	 */
	protected boolean handlePlate(EuclidianView3DInterface ev, LinkedHashMap<String, String> attrs) {
		try {
			String strShowPlate = attrs.get("show");

			// show the plane
			if (strShowPlate != null) {
				boolean showPlate = parseBoolean(strShowPlate);
				ev.setShowPlate(showPlate);
			}
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}	
	
	
	/** handles plane attributes (show plate) for EuclidianView3D
	 * @param ev
	 * @param attrs
	 * @return true if all is done ok
	 */
	protected boolean handleYAxisIsUp(EuclidianView3DInterface ev, LinkedHashMap<String, String> attrs) {
		try {
			String strYAxisVertical = attrs.get("val");

			// show the plane
			if (strYAxisVertical != null) {
				boolean yAxisVertical = parseBoolean(strYAxisVertical);
				ev.setYAxisVertical(yAxisVertical);
			}
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}	
	
	
	
	/** handles plane attributes (show grid) for EuclidianView3D
	 * @param evs euclidian settings
	 * @param attrs
	 * @return true if all is done ok
	 */
	protected boolean handleGrid(EuclidianSettings evs, LinkedHashMap<String, String> attrs) {
		try {
			String strShowGrid = attrs.get("show");

			// show the plane
			if (strShowGrid != null) {
				boolean showGrid = parseBoolean(strShowGrid);
				evs.showGrid(showGrid);
			}
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	/** 
	 * @param evs
	 * @param attrs
	 * @return true if all is done ok
	 */
	protected boolean handleClipping(EuclidianSettings3D evs, LinkedHashMap<String, String> attrs) {
		try {
			String strUseClipping = attrs.get("use");
			if (strUseClipping != null) {
				boolean useClipping = parseBoolean(strUseClipping);
				evs.setUseClippingCube(useClipping);
			}
			String strShowClipping = attrs.get("show");
			if (strShowClipping != null) {
				boolean showClipping = parseBoolean(strShowClipping);
				evs.setShowClippingCube(showClipping);
			}			
			String strSizeClipping = attrs.get("size");
			if (strSizeClipping != null) {
				int sizeClipping = Integer.parseInt(strSizeClipping);
				evs.setClippingReduction(sizeClipping);
			}			
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	/** handles projection attribute
	 * @param ev
	 * @param attrs
	 * @return true if all is done ok
	 */
	protected boolean handleProjection(EuclidianView3DInterface ev, LinkedHashMap<String, String> attrs) {
		try {
			String strType = attrs.get("type");
			if (strType != null) {
				int type = Integer.parseInt(strType);
				ev.setProjection(type);
			}			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** create absolute start point (coords expected) */
	@Override
	protected GeoPointND handleAbsoluteStartPoint(LinkedHashMap<String, String> attrs) {
		double x = Double.parseDouble(attrs.get("x"));
		double y = Double.parseDouble(attrs.get("y"));
		double z = Double.parseDouble(attrs.get("z"));
		
		String wStr = attrs.get("w");
		GeoPointND p;
		if (wStr != null) {		
			// 3D
			double w = Double.parseDouble(wStr);
			p = new GeoPoint3D(cons);
			p.setCoords(x, y, z, w);
		} else {
			// 2D
			p = new GeoPoint(cons);
			p.setCoords(x, y, z);			
		}
		return p;
	}
	
	
	private static GColor handleColorAttrs(LinkedHashMap<String, String> attrs) {
		try {
			int red = Integer.parseInt(attrs.get("r"));
			int green = Integer.parseInt(attrs.get("g"));
			int blue = Integer.parseInt(attrs.get("b"));
			return geogebra.common.factories.AwtFactory.prototype.newColor(red,
					green, blue);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
	@Override
	protected void startEuclidianViewElementCheckViewId(String eName,
			LinkedHashMap<String, String> attrs){
		
		if ("viewId".equals(eName)){
			String plane = attrs.get("plane");
			evSet = app.getSettings().getEuclidianForPlane(plane);
			if (evSet == null){
				evSet = new EuclidianSettingsForPlane(null);
				app.getSettings().setEuclidianSettingsForPlane(plane, evSet);
			}
		}
	}
	
	@Override
	protected boolean startEuclidianViewElementSwitch(String eName,
			LinkedHashMap<String, String> attrs, char firstChar){

		if (firstChar=='t'){
			if ("transformForPlane".equals(eName)) {
				return handleTransformForPlane((EuclidianSettingsForPlane) evSet, attrs);
			} 
		}
		
		return super.startEuclidianViewElementSwitch(eName, attrs, firstChar);
	}
	
	private static boolean handleTransformForPlane(EuclidianSettingsForPlane ev,
			LinkedHashMap<String, String> attrs) {
		
		try {
			ev.setTransformForPlane(
					Boolean.parseBoolean(attrs.get("mirror")),
					Integer.parseInt(attrs.get("rotate")));
			return true;
		} catch (Exception e) {
			return false;
		}

	}
	
	
	@Override
	protected void handleMatrixConicOrQuadric(LinkedHashMap<String, String> attrs) throws Exception{
		if (geo.isGeoQuadric()) {
			GeoQuadric3D quadric = (GeoQuadric3D) geo;
			// set matrix and classify conic now
			// <eigenvectors> should have been set earlier
			double[] matrix = { StringUtil.parseDouble(attrs.get("A0")),
					StringUtil.parseDouble(attrs.get("A1")),
					StringUtil.parseDouble(attrs.get("A2")),
					StringUtil.parseDouble(attrs.get("A3")),
					StringUtil.parseDouble(attrs.get("A4")),
					StringUtil.parseDouble(attrs.get("A5")),
					StringUtil.parseDouble(attrs.get("A6")),
					StringUtil.parseDouble(attrs.get("A7")),
					StringUtil.parseDouble(attrs.get("A8")),
					StringUtil.parseDouble(attrs.get("A9")) };
			quadric.setMatrix(matrix);
		}else{
			super.handleMatrixConicOrQuadric(attrs);
		}
	}

}
