package uebung5;

import java.util.ArrayList;
import java.util.HashMap;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Robot {

	private ContinuousSpace<Object> space;

	private Grid<Object> grid;

	public GridPoint startingPosition;
	public GridPoint currentPosition;
	public ArrayList<GridPoint> previousPositions;
	public boolean done;

	public static final GridPoint GOAL = new GridPoint(8, 4);
	public static int rounds;
	static HashMap<GridPoint, Integer> learnMap = new HashMap<>();
	public int cost;

	public Robot(ContinuousSpace<Object> space, Grid<Object> grid, GridPoint startingPosition) {
		this.space = space;
		this.grid = grid;
		this.cost = 0;

		this.startingPosition = startingPosition;
		this.currentPosition = startingPosition;
		this.previousPositions = new ArrayList<>();
		this.done = false;

	}

	public GridPoint getBestCandidate(boolean ignorePrevious) {
		GridPoint directions[] = new GridPoint[4];

		int cX = currentPosition.getX();
		int cY = currentPosition.getY();

		// left
		if (cX > 0) {
			directions[0] = new GridPoint(cX - 1, cY);
		}
		// right
		if (cX < 9) {
			directions[1] = new GridPoint(cX + 1, cY);
		}
		// down
		if (cY > 0) {
			directions[2] = new GridPoint(cX, cY - 1);
		}
		// up
		if (cY < 4) {
			directions[3] = new GridPoint(cX, cY + 1);
		}
		double bestDistance = Double.MAX_VALUE;
		GridPoint bestCandidate = null;
		for (int i = 0; i < 4; i++) {
			if (directions[i] != null && (ignorePrevious || !previousPositions.contains(directions[i]))) {
				double dist = grid.getDistance(GOAL, directions[i]);
				dist -= (Robot.learnMap.get(directions[i])
						* RunEnvironment.getInstance().getParameters().getDouble("rate"));
				if (dist < bestDistance) {
					bestDistance = dist;
					bestCandidate = directions[i];
				}
			}
		}
		return bestCandidate;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void run() {

		if (rounds > 0 && !done) {
			GridPoint bestCandidate = getBestCandidate(false);

			if (bestCandidate == null) {
				bestCandidate = getBestCandidate(true);
			}
			grid.moveTo(this, bestCandidate.getX(), bestCandidate.getY());
			previousPositions.add(currentPosition);
			currentPosition = bestCandidate;

			int learnValue = Map.singleton.map.get(currentPosition);
			cost -= learnValue;

			Robot.learnMap.put(currentPosition, learnValue);

			if (currentPosition.getX() == GOAL.getX() && currentPosition.getY() == GOAL.getY()) {
				done = true;
				previousPositions.clear();
			}

		}

	}

	public int getCost() {
		return cost;
	}
}
