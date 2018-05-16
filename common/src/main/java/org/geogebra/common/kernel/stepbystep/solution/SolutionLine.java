package org.geogebra.common.kernel.stepbystep.solution;

import org.geogebra.common.kernel.stepbystep.steptree.StepNode;
import org.geogebra.common.main.Localization;

import java.util.ArrayList;
import java.util.List;

public class SolutionLine extends SolutionStep {
	/**
	 * The color of the solution step is either contained in the parameters
	 * themselves, or - when there is no parameter, but there is still need for a
	 * color (to signal for example the regrouping of constants), you have to pass a
	 * color. This will be represented as a dot after the text of the step.
	 */

	private SolutionStepType type;
	private StepNode[] parameters;
	private List<Integer> colors;

	public SolutionLine(SolutionStepType type, StepNode... parameters) {
		this.type = type;
		this.parameters = new StepNode[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			this.parameters[i] = parameters[i].deepCopy();
		}
	}

	public SolutionLine(SolutionStepType type, int color) {
		this.type = type;
		this.colors = new ArrayList<>();

		colors.add(color);
	}

	/**
	 * Get the simple (no colors) text of the step
	 *
	 * @return default text, formatted using LaTeX
	 */
	@Override
	public List<String> getDefault(Localization loc) {
		return type.getDefaultText(loc, parameters);
	}

	/**
	 * Get the detailed (colored) text of the step
	 *
	 * @return colored text, formatted using LaTeX
	 */
	@Override
	public List<String> getDetailed(Localization loc) {
		return type.getDetailedText(loc, colors, parameters);
	}

	/**
	 * Adds a substep
	 *
	 * @param s substep to add
	 */
	@Override
	public void addSubStep(SolutionStep s) {
		if (substeps == null) {
			substeps = new ArrayList<>();
		}

		if (type == SolutionStepType.SUBSTEP_WRAPPER && s instanceof SolutionLine &&
				((SolutionLine) s).parameters == null) {
			SolutionLine line = (SolutionLine) s;

			for (int i = 1; i < substeps.size(); i++) {
				if (substeps.get(i) instanceof SolutionLine &&
						line.type == ((SolutionLine) substeps.get(i)).type) {
					if (line.colors != null) {
						((SolutionLine) substeps.get(i)).colors.addAll(line.colors);
					}

					return;
				}
			}
		}

		substeps.add(s);
	}

	public SolutionStepType getType() {
		return type;
	}
}
