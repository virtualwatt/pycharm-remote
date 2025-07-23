package vvat;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

@Service
public final class BreakpointManagerPlugin implements StartupActivity {
    private BreakpointTelnetServer server;

    @Override
    public void runActivity(@NotNull Project project) {
        server = new BreakpointTelnetServer(2323);
        server.start();
    }

    public void dispose() {
        if (server != null) {
            server.stop();
        }
    }
}
