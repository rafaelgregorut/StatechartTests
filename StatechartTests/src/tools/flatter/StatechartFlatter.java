package tools.flatter;

import java.util.ArrayList;

import xml.statechart.Regions;
import xml.statechart.Statechart;
import xml.statechart.Vertices;

public class StatechartFlatter {

	public Statechart flat(Statechart sc) {
		
		Statechart flat = new Statechart();
		
		ArrayList<Regions> regs = new ArrayList<Regions>();
		for (Regions region : sc.getListRegions()) {
			Regions r = removeHierarchy(region);
			Regions r2 = removeOrthogonality(r);
			//preciso passar a lista de vertices para a regiao principal
			//no final so posso ter uma regiao que sera a principal
		}
		
		flat.setListRegions(regs);
		return flat;
	}

	private Regions removeOrthogonality(Regions reg) {
		// TODO Auto-generated method stub
		return null;
	}

	private Regions removeHierarchy(Regions reg ) {

		Regions regFlat = new Regions();
		ArrayList<Vertices> vers = new ArrayList<Vertices>();
		for (Vertices vertice : reg.getListVertices()) {
			
			//verifico se esse vertice possui outras regioes dentro dele
			//se tiver eh pq tem sub-estados
			if (vertice.getListRegions().size() > 0) {
				// passa para o sub-estados as transicoes que saem do estado pai
				
				//passo as transicoes que entram no estado pai para o estado filho inicial
				
				//removo a hierarquia dos estados filhos
				//chamo recursivamente o removeHierarchy para cada regiao da lista desse vertice
			
				//adiciono o estado filho na lista da regiao planificada
			}
			//posso adicionar o vertice na lista da regiao planificada
			
		}
		
		return null;
	}
	
}
