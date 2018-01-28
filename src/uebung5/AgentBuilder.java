package uebung5;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;

public class AgentBuilder implements ContextBuilder<Object> {

	@Override
	public Context<Object> build(Context<Object> context) {
		context.setId("Uebung5");

		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context,
				new RandomCartesianAdder<Object>(), new repast.simphony.space.continuous.StrictBorders(), 10, 5);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);

		// ebenfalls StrictBorder
		Grid<Object> grid = gridFactory.createGrid("grid", context,

				new GridBuilderParameters<Object>(new StrictBorders(), new SimpleGridAdder<Object>(), true, 10, 5));

		for (int i = 0; i <= 9; i++) {
			for (int j = 0; j <= 4; j++) {
				Robot.learnMap.put(new GridPoint(i, j), -1);
			}
		}
		Robot.learnMap.put(new GridPoint(3, 1), Integer.MIN_VALUE);
		Robot.learnMap.put(new GridPoint(3, 2), Integer.MIN_VALUE);
		Robot.learnMap.put(new GridPoint(6, 4), Integer.MIN_VALUE);
		Robot.learnMap.put(new GridPoint(7, 3), Integer.MIN_VALUE);
		Robot.learnMap.put(new GridPoint(7, 4), Integer.MIN_VALUE);

		Robot a1 = new Robot(space, grid, new GridPoint(0, 0));
		Robot a2 = new Robot(space, grid, new GridPoint(0, 4));

		Robot.rounds = RunEnvironment.getInstance().getParameters().getInteger("rounds");
		Map m = new Map(space, grid);
		m.addAgent(a1);
		m.addAgent(a2);

		context.add(a1);
		context.add(a2);
		context.add(m);

		grid.moveTo(a1, a1.startingPosition.getX(), a1.startingPosition.getY());
		grid.moveTo(a2, a2.startingPosition.getX(), a2.startingPosition.getY());

		return context;
	}

}
