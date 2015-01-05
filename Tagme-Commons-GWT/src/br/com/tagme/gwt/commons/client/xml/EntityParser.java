package br.com.tagme.gwt.commons.client.xml;

import java.util.Map;

import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import br.com.tagme.gwt.commons.client.components.GenericData;
import br.com.tagme.gwt.commons.client.xml.MetadataParser.MetadataField;
import br.com.tagme.gwt.commons.utils.client.StringUtils;
import br.com.tagme.gwt.commons.utils.client.XMLUtils;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class EntityParser {

	public static void buildFromXml(Element response, FormItemBuilder builder){
		
		Node entidades = response.getElementsByTagName("entidades").item(0);
		int totalEntidades = Integer.parseInt(((Element) entidades).getAttribute("total"));
		
		for(int i = 0; i < totalEntidades; i++){
			Element entidade = (Element) response.getElementsByTagName("entidade").item(i);
			GenericData genericData = new GenericData();
			NodeList listFields = entidade.getChildNodes();
			for (int j = 0; j < listFields.getLength(); j++) {
				if (j % 2 == 0) {
					j = j + 1;
				}
				if (j != listFields.getLength()) {
					Element field = (Element) listFields.item(j);
					String value = XMLUtils.getNodeValue(field);
					if(StringUtils.isEmpty(value)){
						value = "-";
					}
					genericData.addValue(field.getNodeName(), value);
				}
			}
			builder.buildItem(genericData);
		}
	}
	
	public static CellTable<GenericData> buildCellTable(Element response){
		
		CellTable<GenericData> grid = new CellTable<GenericData>(10);
		
		grid.setBordered(true);
		grid.setCondensed(true);
		grid.setHover(true);
		final SimplePager pager = new SimplePager();
		ListDataProvider<GenericData> dataProvider = new ListDataProvider<GenericData>();
		final Pagination pagination = new Pagination();
		
		Node entidades = response.getElementsByTagName("entidades").item(0);
		int totalEntidades = Integer.parseInt(((Element) entidades).getAttribute("total"));
		
		Node metadata = response.getElementsByTagName("metadata").item(0);
		
		Map<String, MetadataField> mapMetadata = MetadataParser.parse(metadata);
		for(String currentMetadataFieldName : mapMetadata.keySet()){
			final MetadataField metadataField = mapMetadata.get(currentMetadataFieldName);
			if(metadataField.isVisible()){
				TextColumn<GenericData> col = new TextColumn<GenericData>(){

					@Override
					public String getValue(GenericData genericData) {
						return genericData.getValueAsString(metadataField.getFieldName());
					}
					
				};
				
				grid.addColumn(col, metadataField.getLabel());
			}
		}
		
		grid.addRangeChangeHandler(new RangeChangeEvent.Handler(){

			@Override
			public void onRangeChange(RangeChangeEvent event) {
				pagination.rebuild(pager);
			}
			
		});
		
		pager.setDisplay(grid);
		pagination.clear();
		dataProvider.addDataDisplay(grid);
		
		NodeList listEntidades = response.getElementsByTagName("entidade");
		for(int i = 0; i < totalEntidades; i++){
			Node entidade = listEntidades.item(i);
			GenericData genericData = new GenericData();
			NodeList listFields = entidade.getChildNodes();
			for (int j = 0; j < listFields.getLength(); j++) {
				if (j % 2 == 0) {
					j = j + 1;
				}
				if (j != listFields.getLength()) {
					Element field = (Element) listFields.item(j);
					genericData.addValue(field.getNodeName(), XMLUtils.getNodeValue(field));
				}
			}
			
			dataProvider.getList().add(genericData);
		}
		
		dataProvider.flush();
		pagination.rebuild(pager);
			
		return grid;
		
	}

	public interface FormItemBuilder {
		
		void buildItem(GenericData genericData);
		
	}
}
