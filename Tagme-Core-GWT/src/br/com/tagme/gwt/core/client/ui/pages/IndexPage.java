package br.com.tagme.gwt.core.client.ui.pages;

import java.util.ArrayList;
import java.util.HashMap;

import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.constants.IconType;

import br.com.tagme.gwt.commons.client.DefaultCompositeWithPresenter;
import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.ActivityController;
import br.com.tagme.gwt.core.client.CoreEntryPoint;
import br.com.tagme.gwt.core.client.place.AtendimentoOnlinePlace;
import br.com.tagme.gwt.core.client.place.BoletoPlace;
import br.com.tagme.gwt.core.client.place.ConsultaOSPlace;
import br.com.tagme.gwt.core.client.place.InformacoesCadastraisPlace;
import br.com.tagme.gwt.core.client.ui.ClickableThumbnailWidget;
import br.com.tagme.gwt.core.client.ui.InfoThumbnailWidget;
import br.com.tagme.gwt.core.client.ui.ThumbnailWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class IndexPage extends DefaultCompositeWithPresenter {

	private static IndexPageUiBinder	uiBinder	= GWT.create(IndexPageUiBinder.class);

	interface IndexPageUiBinder extends UiBinder<Widget, IndexPage> {
	}
	
	@UiField Column thumbPos1;
	@UiField Column thumbPos2;
	@UiField Column thumbPos3;
	@UiField Column thumbPos4;
	@UiField Column thumbPos5;
	@UiField Column thumbPos6;
	@UiField Column thumbPos7;
	@UiField Column thumbPos8;
	
	public IndexPage(DefaultActivity presenter) {
		super(presenter);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		final HashMap<String, Column> thumbnailsPositions = new HashMap<String, Column>();
		final ArrayList<ThumbnailWidget> thumbnails = new ArrayList<ThumbnailWidget>();
		
		thumbnails.add(new ClickableThumbnailWidget(IconType.BUILDING_O, "Informações Cadastrais", "Atualização de informações cadastrais de sua organização, CNPJ's licenciados e contatos...", new InformacoesCadastraisPlace("home")));
		thumbnails.add(new ClickableThumbnailWidget(IconType.COMMENTS, "Atendimento Online", "Entre em contato com o Help Desk Especializado e obtenha suporte imediato de nossos atendentes...", new AtendimentoOnlinePlace("home")));
		thumbnails.add(new ClickableThumbnailWidget(IconType.FILE_TEXT, "Ordens de Serviço", "Inclusão e consulta de ordens de serviço, históricos e indicadores de seus atendimentos...", new ConsultaOSPlace("home")));
		thumbnails.add(new ClickableThumbnailWidget(IconType.USD, "Movimentações Financeiras", "Emissão de 2ª via de boletos, consulta ao extrato de movimentações...", new BoletoPlace("home")));
		thumbnails.add(new InfoThumbnailWidget(IconType.PRINT, "Autorização de ECF", "Gerar e baixar os arquivos necessários para configurar sua Emissora de Cupom Fiscal..."));
		thumbnails.add(new InfoThumbnailWidget(IconType.STAR, CoreEntryPoint.isJiva() ? "Soluções Jiva" : "Soluções Sankhya", "Confira as soluções já disponíveis para sua organização, quantidade de usuários licenciados..."));
		thumbnails.add(new InfoThumbnailWidget(IconType.ARCHIVE, "Projetos", "Analise a completude das etapas de seus projetos, com cronogramas, pendências e escopos..."));
		thumbnails.add(new InfoThumbnailWidget(IconType.QUOTE_LEFT, "Satisfação", "Envie seu depoimento e compartilhe com todos sua visão sobre nossos serviços e soluções..."));
		
		thumbnailsPositions.put("1", thumbPos1);
		thumbnailsPositions.put("2", thumbPos2);
		thumbnailsPositions.put("3", thumbPos3);
		thumbnailsPositions.put("4", thumbPos4);
		thumbnailsPositions.put("5", thumbPos5);
		thumbnailsPositions.put("6", thumbPos6);
		thumbnailsPositions.put("7", thumbPos7);
		thumbnailsPositions.put("8", thumbPos8);
		
		int i = 0;
		for(ThumbnailWidget thumbnail : thumbnails){
			i++;
			
			if(thumbnail instanceof ClickableThumbnailWidget){
				AbstractPlace targetPlace = ((ClickableThumbnailWidget) thumbnail).getTargetPlace();
				if(targetPlace == null || ActivityController.AUTHZ_TYPE_AUTHORIZED != AppAuth.getInstance().checkAuthorization(targetPlace)){
					((ClickableThumbnailWidget) thumbnail).removeLink();
				}
			}
			
			thumbnailsPositions.get(String.valueOf(i)).add(thumbnail);
			
		}
		
	}

}
