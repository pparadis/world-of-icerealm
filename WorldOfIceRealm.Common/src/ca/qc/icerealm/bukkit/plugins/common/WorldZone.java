package ca.qc.icerealm.bukkit.plugins.common;

import org.bukkit.Location;

/**
 * Permet de d�finir une zone dans le monde.
 * IMPORTANT: s'assurer que les changements que vous ferez sont backward
 * compatible aka, don't break existing code!!!
 */
public class WorldZone {
	
	private Location _rightBottom;
	private Location _leftTop;

	public WorldZone(Location lt, Location rb) {
		_leftTop = lt;
		_rightBottom = rb;
	}
	
	public boolean isInside(Location position) {

		boolean inside = false;
		
		if (_leftTop.getX() < position.getX() && _leftTop.getZ() < position.getZ() &&
			_rightBottom.getX() > position.getX() && _rightBottom.getZ() > position.getZ()) {
			inside = true;
		}
		return inside;
	}
	
	public boolean isInside(double x1, double y1, double x2, double y2) {
		boolean inside = false;
		
		if (_leftTop.getX() < x1 && _leftTop.getZ() < y1 &&
			_rightBottom.getX() > x2 && _rightBottom.getZ() > y2) {
			inside = true;
		}
		return inside;
	}
	
	public Location getTopLeft() {
		return _leftTop;
	}
	
	public Location getRightBottom() {
		return _rightBottom;
	}
	
	public Location getRelativeBottomRight() {
		Location relativeBottomRight = new Location(_rightBottom.getWorld(), 
													_rightBottom.getX() - _leftTop.getX(), 
													_rightBottom.getY(), 
													_rightBottom.getZ() - _leftTop.getZ());
		return relativeBottomRight;
		
		
	}
	
	public Location getCentralPointAt(double height) {
		double x = (this._leftTop.getX() + this._rightBottom.getX()) / 2;
		double z = (this._leftTop.getZ() + this._rightBottom.getZ()) / 2;
		return new Location(_leftTop.getWorld(), x, height, z);
	}
	
	@Override 
	public String toString() {
		return "Top Left (" + _leftTop.getX() + "," + _leftTop.getZ() + " Right Bottom (" + _rightBottom.getX() + "," + _rightBottom.getZ();
	}
	
}
