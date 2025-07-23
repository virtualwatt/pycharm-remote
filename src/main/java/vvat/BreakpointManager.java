package vvat;

import com.intellij.debugger.impl.DebuggerManagerImpl;
import com.intellij.debugger.ui.breakpoints.Breakpoint;
import com.intellij.debugger.ui.breakpoints.BreakpointManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;

import java.util.Collection;

public class BreakpointManager {
    public static void enableAllBreakpoints() {
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            BreakpointManager breakpointManager = DebuggerManagerImpl.getInstance(project).getBreakpointManager();
            Collection<Breakpoint> breakpoints = breakpointManager.getBreakpoints();
            for (Breakpoint breakpoint : breakpoints) {
                breakpoint.setEnabled(true);
            }
        }
    }

    public static void disableAllBreakpoints() {
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            BreakpointManager breakpointManager = DebuggerManagerImpl.getInstance(project).getBreakpointManager();
            Collection<Breakpoint> breakpoints = breakpointManager.getBreakpoints();
            for (Breakpoint breakpoint : breakpoints) {
                breakpoint.setEnabled(false);
            }
        }
    }
}
