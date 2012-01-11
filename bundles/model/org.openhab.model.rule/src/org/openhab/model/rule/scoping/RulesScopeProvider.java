/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
/*
 * generated by Xtext
 */
package org.openhab.model.rule.scoping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.MapBasedScope;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XVariableDeclaration;
import org.eclipse.xtext.xbase.scoping.LocalVariableScopeContext;
import org.openhab.model.rule.rules.Rule;
import org.openhab.model.rule.rules.RuleModel;
import org.openhab.model.script.scoping.ScriptScopeProvider;


/**
 * This scope provider adds all things to the scope which are specific to rules.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@SuppressWarnings("restriction")
public class RulesScopeProvider extends ScriptScopeProvider {
	
	@Override
	protected IScope createLocalVarScope(IScope parentScope,
			LocalVariableScopeContext scopeContext) {
		if(scopeContext.getContext() instanceof Rule) {
			IScope parent = super.createLocalVarScope(parentScope, scopeContext);
			List<IEObjectDescription> descriptions = new ArrayList<IEObjectDescription>();
			descriptions.addAll(createVarFeatures(scopeContext.getContext().eResource()));
	
			return MapBasedScope.createScope(parent, descriptions);
		} else {
			return super.createLocalVarScope(parentScope, scopeContext);
		}
	}
	
	private Collection<? extends IEObjectDescription> createVarFeatures(Resource resource) {
		List<IEObjectDescription> descriptions = new ArrayList<IEObjectDescription>();

		if(resource.getContents().size()>0 && resource.getContents().get(0) instanceof RuleModel) {
			RuleModel ruleModel = (RuleModel) resource.getContents().get(0);
			for(XExpression expr : ruleModel.getVariables()) {
				if (expr instanceof XVariableDeclaration) {
					XVariableDeclaration var = (XVariableDeclaration) expr;
					if(var.getName()!=null && var.getType()!=null) {
						descriptions.add(createLocalVarDescription(var));
					}
				}
			}
		}
		
		return descriptions;
	}
}
