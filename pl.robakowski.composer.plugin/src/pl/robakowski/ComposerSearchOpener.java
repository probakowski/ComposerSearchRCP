//******************************************************************
//
//  ComposerSearchOpener.java
//  Copyright 2014 PSI AG. All rights reserved.
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package pl.robakowski;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.window.IShellProvider;

public class ComposerSearchOpener {
	@Execute
	public void execute(EPartService partService, IShellProvider shell) {
		partService.showPart("composerSearch", PartState.ACTIVATE);
	}
}
