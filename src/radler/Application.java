package radler;

import radler.gui.CloseAction;
import radler.gui.ObjectSearch;
import radler.gui.OpenAction;
import radler.gui.UiClassResolver;
import radler.persistence.GenericDataProvider;

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

    private Map<String, Class<?>> _classes = new HashMap<String, Class<?>>();
    private Map<Class<?>, UiClassResolver> _resolvers = new HashMap<Class<?>, UiClassResolver>();
    private JTabbedPane _tabbedPane = new JTabbedPane();

    private Map<String, JComponent> _openTabs = new HashMap<String, JComponent>();

    private GenericDataProvider _dataProvider;

    public Application(Class<?>[] classes) {
        for (Class<?> clazz : classes) {
            _classes.put(clazz.getName(), clazz);
            _resolvers.put(clazz, new UiClassResolver(clazz));
        }
        _dataProvider = new GenericDataProvider(_resolvers);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(640, 480));
        init();
        setVisible(true);
    }

    private void init() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Admin");
        for (Map.Entry<String, Class<?>> entry : _classes.entrySet()) {
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
            Class<?> clazz = _classes.get(menuItem.getName());
            UiClassResolver uiClassResolver = _resolvers.get(clazz);
            _tabbedPane.add(uiClassResolver.getTitle(), new ObjectSearch(uiClassResolver, _dataProvider,
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
            }, new CloseAction() {
                @Override
                public void close(JComponent component) {
                    _openTabs.remove(component.getName());
                    _tabbedPane.remove(component);
                }
            }));
            _tabbedPane.setVisible(true);
            _tabbedPane.doLayout();
        }
    }
}
