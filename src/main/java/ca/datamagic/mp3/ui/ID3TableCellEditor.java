/**
 * 
 */
package ca.datamagic.mp3.ui;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import ca.datamagic.id3.dto.ID3TagDTO;

/**
 * @author Greg
 *
 */
public class ID3TableCellEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = 1L;
	private JComboBox<String> _editor = new JComboBox<String>(ID3TagDTO.ID3VERSIONS);

	@Override
	public Object getCellEditorValue() {
		return _editor.getSelectedItem();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex) {
		// Set the model data of the table
	    if(isSelected) {
	    	_editor.setSelectedItem(value);
		    TableModel model = table.getModel();
		    model.setValueAt(value, rowIndex, colIndex);
	    }
	    return _editor;
	}
}
