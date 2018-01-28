package uebung5;

import java.util.ArrayList;
import java.util.HashMap;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Map {

	private ContinuousSpace<Object> space;

	private Grid<Object> grid;

	HashMap<GridPoint, Integer> map;

	public static Map singleton;

	private ArrayList<Robot> agents;

	public Map(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
		agents = new ArrayList<>();

		this.map = new HashMap<>();

		for (int i = 0; i <= 9; i++) {
			for (int j = 0; j <= 4; j++) {
				this.map.put(new GridPoint(i, j), -1);
			}
		}

		// slippery
		this.map.put(new GridPoint(0, 3), -3);
		this.map.put(new GridPoint(3, 3), -3);
		this.map.put(new GridPoint(3, 4), -3);
		this.map.put(new GridPoint(5, 2), -3);
		this.map.put(new GridPoint(6, 1), -3);
		this.map.put(new GridPoint(7, 0), -3);

		// trap
		this.map.put(new GridPoint(1, 0), -10);
		this.map.put(new GridPoint(2, 2), -10);
		this.map.put(new GridPoint(8, 3), -10);
		this.map.put(new GridPoint(9, 1), -10);

		// impassable
		this.map.put(new GridPoint(3, 1), Integer.MIN_VALUE);
		this.map.put(new GridPoint(3, 2), Integer.MIN_VALUE);
		this.map.put(new GridPoint(6, 4), Integer.MIN_VALUE);
		this.map.put(new GridPoint(7, 3), Integer.MIN_VALUE);
		this.map.put(new GridPoint(7, 4), Integer.MIN_VALUE);

		// exit
		this.map.put(new GridPoint(8, 4), 0);

		if (Map.singleton == null) {
			Map.singleton = this;
		}
	}

	public void addAgent(Robot r) {
		agents.add(r);
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void checkForFinish() {
		boolean reset = true;
		for (Robot robot : agents) {
			if (!robot.done) {
				reset = false;
				break;
			}
		}

		if (reset) {
			Robot.rounds--;
			for (Robot robot : agents) {
				grid.moveTo(robot, robot.startingPosition.getX(), robot.startingPosition.getY());
				robot.currentPosition = robot.startingPosition;
				robot.done = false;
				robot.cost = 0;
			}
		}
	}
}
