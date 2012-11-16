package radler;

import radler.gui.MetaModel;
import radler.gui.ObjectSearch;
import radler.gui.actions.CancelAction;
import radler.gui.actions.OpenAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class Application extends JFrame implements ActionListener {

    private JTabbedPane _tabbedPane = new JTabbedPane();
    private Map<String, JComponent> _openTabs = new HashMap<String, JComponent>();

    private ApplicationFactory _applicationFactory = ApplicationFactory.getInstance();

    public Application(Class<?>[] classes) {
        ApplicationFactory.init(classes);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(640, 480));
        init();
        setVisible(true);
    }

    private void init() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Admin");
        for (Map.Entry<String, Class<?>> entry : ApplicationFactory.getInstance().getClasses().entrySet()) {
            JMenuItem item = new JMenuItem(entry.getValue().getSimpleName());
            item.setName(entry.getKey());
            item.addActionListener(this);
            menu.add(item);
        }
        menuBar.add(menu);
        setJMenuBar(menuBar);
        _tabbedPane.setVisible(false);
        add(_tabbedPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) e.getSource();
            Class<?> clazz = _applicationFactory.getClasses().get(menuItem.getName());
            MetaModel metaModel = _applicationFactory.getResolvers().get(clazz);
            _tabbedPane.add(metaModel.getTitle(), new ObjectSearch(metaModel, _applicationFactory.getDataProvider(),
                    new OpenAction() {
                @Override
                public void open(JComponent component) {
                    if (!_openTabs.containsKey(component.getName())) {
                        _tabbedPane.add(component);
                        _openTabs.put(component.getName(), component);
                    } else {
                        component = _openTabs.get(component.getName());
                    }
                    _tabbedPane.setSelectedComponent(component);
                }
            }, new CancelAction() {
                @Override
                public void onCancel(JComponent component) {
                    _openTabs.remove(component.getName());
                    _tabbedPane.remove(component);
                }
            }));
            _tabbedPane.setVisible(true);
            _tabbedPane.doLayout();
        }
    }
}
