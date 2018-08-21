/*
 * Name       : Power Tour TINY
 * Date       : June 2012
 * Owner      : CreativeMilk
 * Url        : www.jquerypowertour.com
 * Version    : 3.2.0
 * Updated    : 2016-07-29 17:12:30 UTC+02:00
 * Developer  : Mark
 * Dependency : 
 * Lib        : jQuery 1.7+
 * Licence    : NOT free, http://codecanyon.net/item/power-tour-powerfull-creative-jquery-tour-plugin/3246071
 */
 
;(function(root, factory){
	
	if(typeof define === 'function' && define.amd){
		define(['jquery'], factory);
	}else{
		factory(root.jQuery);
	}
	
}(this, function($){		  
	
	//"use strict"; // jshint ;_;
	
	var pluginName = 'powerTour';
	
	function Plugin(element, options){
		
		/**
		* Variables.
		**/	
		this.obj = $(element);		
		this.o   = $.extend({}, $.fn[pluginName].defaults, options);

		this.init();
	};

	Plugin.prototype = {
								
		/**	
		* INIT.
		**/	
		init: function(){
			
			var self   = this;
			
			/**
			* Global variables.
			**/	
			bd                = $('body');
			clickEvent        = 'click touchstart';
			screenPos         = new Array('sc','stl','stm','str','srm','sbr','sbm','sbl','slm');
			cdInterval        = '';
			cssAnimationSpeed = 1000;
			d_pwac            = 'data-powertour-action';
			d_pwcs            = 'data-powertour-currentstep';
			d_pwid            = 'data-powertour-tour'; 
			d_pwps            = 'data-powertour-previousstep';
			d_pwrn            = 'data-powertour-run';
			d_pwst            = 'data-powertour-step'; 
			d_pwsw            = 'data-powertour-startwith'; 
			d_pwtg            = 'data-powertour-trigger';
			c_pwsw            = 'powertour-show powertour-fxin';
			c_pwhd            = 'powertour-hide powertour-fxout';			
			c_pwhl            = 'powertour-highlight';			
			c_pwhk            = 'powertour-hook';
			c_pwdc            = 'powertour-disable-cancel';
			c_pwst            = 'powertour-step';
			c_pwmk            = 'powertour-mask';
			c_pwah            = 'powertour-activehook';
			c_pwas            = 'powertour-activestep';
			
			/**	
			* Append/prepend the mask to the body. 
			**/
			if($('#'+c_pwmk).length < 1){
					
				var msk = '<div id="'+c_pwmk+'"></div>';
						
				if(self.o.appendMask != undefined && self.o.appendMask === false){
					bd.prepend(msk);	
				}else{
					bd.append(msk);	
				}				
			}
			
			
			/**	
			* All methods that need to run at init. 
			**/
			self._build();
			self._clickEvents();
			self._keyboardEvents();
			self._runOnStart();

		},		

		/**
		* BUILD TOUR.
		*
		* All logic to build(wrap the steps and such) a tour.
		**/
		_build: function(){ 
		
			var self = this;
			
			/**
			* Create all steps with a loop.
			**/	
			$.each(self.o.tours, function(i, t){

				var sw  = $(t.trigger).attr(d_pwsw);
				var i   = parseInt(i + 1);
				
				/**
				* Use option or pre set value.
				**/	
				if(isNaN(sw)){
					var startWith = (t.startWith !== undefined && $.trim(t.startWith) != '' && t.startWith != '0') ? t.startWith : 1;	
				}else{
					var startWith = sw;	
				}

				/**
				* Set tour trigger meta.
				**/				
				$(t.trigger).attr({
					'data-powertour-startwith' : startWith,
					'data-powertour-trigger'   : i
				})
				.addClass(c_pwdc);				
				
				/**
				* Build every single step.
				**/	
				$.each(t.steps, function(ii, s){				
					
					var ii = parseInt(ii + 1);
					
					/**
					* Setting the position.
					**/	
					self._setPosition(s, i, ii, t, true);

				});	
			});		
		},

		/**
		* SET POSITION.
		*
		* Step the position of each step.
		*
		* @param: s  | string  | The step.
		* @param: i  | integer | Tour index.
		* @param: ii | integer | Step index.



		* @param: t  | object  | Tour object.
		* @param: b  | boolean | Prevent second build if called.
		**/
		_setPosition: function(s, i, ii, t, b){
			
			var self   = this;
			var stepId = '['+d_pwid+'="'+i+'"]['+d_pwst+'="'+ii+'"]';

			/**
			* If value is option is not present use stepdefault value(s) if these are present if not use default values.
			**/		

		    var width     = (s.width     !== undefined && $.trim(s.width)     != '') ? s.width     : 300;
			var position  = (s.position  !== undefined && $.trim(s.position)  != '') ? s.position  : 'tl'
			var Y         = (s.offsetY   !== undefined && $.trim(s.offsetY)   != '') ? s.offsetY   : 0;
			var X         = (s.offsetX   !== undefined && $.trim(s.offsetX)   != '') ? s.offsetX   : 0;
			var highLight = (s.highlight !== undefined && $.trim(s.highlight) != '') ? s.highlight : false;
			 
			/**
			* Check for screen postions, they should 
			* not use an hook, so append it to the body instead.
			**/					
			if($.inArray(position, screenPos) == -1){
					
				var hook = $(s.hookTo);
					
				/**
				* Add a relitive class for the highlight function.
				**/	
				hook.addClass(c_pwhk);
					
			}else{

				/**
				* Attach to body(dummy).
				**/
				var hook = bd;								
			}

			if(b === true){	
			
				/**
				* Build the powerTour template.
				**/
				var tourTemp 
				= '<div class="'+c_pwst+' '+c_pwdc+'" '+d_pwid+'="'+i+'" '+d_pwst+'="'+ii+'" id="pw-'+i+'-'+ii+'" tabindex="-1" role="dialog" aria-hidden="true"></div>';
				
				/**
				* Create the single step of the tour.
				**/	
				hook
				.append(tourTemp)
				.children(stepId)
				.width(width)
				.html($(s.content))
				.children()
				.show();	
		    }
			
			/**
			* Set the main variable.
			**/	
			var step = $(stepId);
			
			/**
			* Getting width and height from the hook and step.
			**/	
			var hookWidth  = hook.outerWidth();
			var hookHeight = hook.outerHeight();	
			var stepWidth  = step.outerWidth();
			var stepHeight = step.outerHeight();

			/**
			* Check and set
			**/			
			switch(position){
				// top left
				case 'tl':
					step.css({left: X , top: -stepHeight - Y});					
				break;
				// top middle
				case 'tm':
					step.css({left: '50%', marginLeft: -(stepWidth/2) - X, top: -stepHeight - Y});					
				break;
				// top right
				case 'tr':
					step.css({right: X , top: -stepHeight - Y});					
				break;
	
				// right top
				case 'rt':
					step.css({left: hookWidth + X, top: Y});
				break;
				// right middle
				case 'rm':
					step.css({left: hookWidth + X, top: '50%', marginTop: -(stepHeight/2) - Y});						
				break;						
				// right bottom
				case 'rb':
					step.css({left: hookWidth + X, bottom: Y});						
				break;
				
				// bottom left
				case 'bl':
					step.css({left: X , bottom: -stepHeight - Y});					
				break;
				// bottom middle
				case 'bm':
					step.css({left: '50%', marginLeft: -(stepWidth/2) - X, bottom: -stepHeight - Y});					
				break;
				// bottom right
				case 'br':
					step.css({right: X , bottom: -stepHeight - Y});					
				break;
	
				// left top
				case 'lt':
					step.css({right: hookWidth + X, top: Y});
				break;
				// left middle
				case 'lm':
					step.css({right: hookWidth + X, top: '50%', marginTop: -(stepHeight/2) - Y});						
				break;						
				// left bottom
				case 'lb':
					step.css({right: hookWidth + X, bottom: Y});						
				break;
	
				// screen center
				case 'sc':
					step.css({left: '50%', top: '50%', marginLeft: -(width/2) - X, marginTop: -(stepHeight/2) - Y, position: 'fixed'});						
				break;						
					
				// screen top left
				case 'stl':
					step.css({left: 20 - X, top: 20 - Y, position: 'fixed'});						
				break;
				// screen top middle
				case 'stm':
					step.css({left: '50%', marginLeft: -(width/2) - X, top: 20 - Y, position: 'fixed'});						
				break;							
				// screen top right
				case 'str':
					step.css({right: 20 - X, top: 20 - Y, position: 'fixed'});						
				break;
				// screen right mid
				case 'srm':
					step.css({right: 20 - X, top: '50%', marginTop: -(stepHeight/2) - Y, position: 'fixed'});						
				break;							
				// screen bottom right
				case 'sbr':
					step.css({right: 20 - X, bottom: 20 - Y, position: 'fixed'});						
				break;
				// screen top middle
				case 'sbm':
					step.css({left: '50%', bottom: 20 - Y, marginLeft: -(width/2) - X, position: 'fixed'});						
				break;
				// screen bottom left
				case 'sbl':
					step.css({left: 20 - X, bottom: 20 - Y, position: 'fixed'});						
				break;									 							
				// screen left mid
				case 'slm':
					step.css({left: 20 - X, top: '50%', marginTop: -(stepHeight/2) - Y, position: 'fixed'});						
				break;								
				
				// no position
				case false:
					// do nothing
				break;
				// right top
				default:
					step.css({right: hookWidth + X, top: Y});
				break;																																				
			};
			
			/**
			* Add highlight class to the hook.
			**/	
			if(highLight === true){
				hook.addClass(c_pwhl);
			}
		},
		
		/**
		* TOUR DATA VARIABLES.
		*
		* Get all main tour and step data here and make them available als global vars.
		**/
		_tourDataVars: function(){
			
			var self  = this;
			id        = bd.attr(d_pwid);
			
			if(!isNaN(id)){

				cs                = bd.attr(d_pwcs);
				ps                = bd.attr(d_pwps);
				
				oid               = (id == 1) ? 1 : id;
				id                = (id == 0) ? 0 : id - 1;
								
				ocs               = (cs == 1) ? 1 : cs;
				cs                = (cs == 0) ? 1 : cs - 1;
			
				if(ps !== undefined){					
					ops           = (ps == 1) ? 1 : ps;
					ps            = (ps == 0) ? 1 : ps - 1;
				}else{
					ops = ps; 
				}		
			    					
				tour              = self.o.tours[id];
				step              = tour.steps[cs];
				
				hook              = tour.steps[cs].hookTo;
				scrollHorizontal  = tour.scrollHorizontal;
								
				highlightStartSpeed = tour.highlightStartSpeed !== undefined && !isNaN(tour.highlightStartSpeed) ? tour.highlightStartSpeed : 0;
				highlightEndSpeed   = tour.highlightEndSpeed   !== undefined && !isNaN(tour.highlightEndSpeed)   ? tour.highlightEndSpeed   : 0;
				
				countSteps        = tour.steps.length; 

				csObj             = $('['+d_pwst+'='+ocs+']['+d_pwid+'='+oid+']');	
				psObj             = $('['+d_pwst+'='+ops+']['+d_pwid+'='+oid+']');
				
				position          = (step.position        !== undefined && $.trim(step.position)        != '') ? step.position        : 'tl';
				center            = (step.center          !== undefined && $.trim(step.center)          != '') ? step.center          : 'step';
				scrollEasing      = (step.scrollEasing    !== undefined && $.trim(step.scrollEasing)    != '') ? step.scrollEasing    : 'swing';
				scrollDelay       = (step.scrollDelay     !== undefined && $.trim(step.scrollDelay)     != '') ? step.scrollDelay     : 0;								
				highlight         = (step.highlight       !== undefined && $.trim(step.highlight)       != '') ? step.highlight       : false;							
				showStepDelay     = (step.showStepDelay   !== undefined && $.trim(step.showStepDelay)   != '') ? step.showStepDelay   : 0;			

				/**
				* Preious step data.
				**/	
				if(ps !== undefined){
					prevStep        = tour.steps[ps];
				}
			}
		},

		/**
		* FXIN.
		*
		* Run the CSS3 transitions(if present). 
		*
		* @param: csObj | object | Previous step object(index).
		**/
		_fxIn: function(csObj){ 	

			if(bd.attr(d_pwid) !== undefined){				
				
				$('.'+c_pwah).removeClass(c_pwah);			

				csObj.parents('.'+c_pwhk).addClass(c_pwah);
				
				var show = c_pwsw;
								
				csObj.attr('class', c_pwst+' '+c_pwdc+' '+show+' '+c_pwas);								
			}
		},
		
		/**
		* FXOUT.
		*
		* Run the CSS3 transitions(if present). 
		*
		* @param: psObj | object | Previous step object(index).
		**/
		_fxOut: function(psObj){ 		

			if(bd.attr(d_pwid) !== undefined){

				var hide = '';				
		
				psObj.attr('class', c_pwst+' '+c_pwhd+' '+hide+' '+c_pwas);
				
				setTimeout(function(){
					psObj.removeClass(c_pwas);	
				},cssAnimationSpeed);
	
			}
		
		},
		
		/**
		* GO TO.
		*
		* Go to the step or hook.
		*
		* @param: hook             | object  | Current hook.
		* @param: step             | object  | Current step.
		* @param: position         | string  | Position of the step.
		* @param: center           | string  | Choose the center(step or hook).
		* @param: highlight        | boolean | Highlight hook and step.
		* @param: scrollHorizontal | boolean | Scroll direction horizontal.
		**/
		_goTo: function(hook, step, center, position, highlight, scrollHorizontal){
		
			var self = this;
						
			/**
			* Reset/hide and show the mask(highlight).
			**/	
			function resetHighlight(){	

				if(highlight === true){
					 
				}else{
					$('#'+c_pwmk).fadeOut(highlightEndSpeed);
				}
				
			}
			resetHighlight();
			
			/**
			* Reset current step, CSS3 animation class must be removed due wrong offset values.
			**/	
			step.attr('class', c_pwst);

			/**
			* Reset previouse exclude class.
			**/	
			$('.'+c_pwhk+'.'+c_pwdc).removeClass(c_pwdc);
		
			/**
			* Choose direction.
			**/	
			if($.inArray(position, screenPos) == -1){
					
				var winHeight = $(window).height();
				var winWidth  = $(window).width();	
					
				if(scrollHorizontal === true){
					if(center == 'step' || $(hook).outerWidth() >= winWidth){ 
						var centerTo = step.show().offset().left - (winWidth/2) + (step.outerWidth()/2);//.show() = important!
					}else{
						var centerTo = $(hook).offset().left - (winWidth/2) + ($(hook).outerWidth()/2); 	
					}

					var scrollDir = { scrollLeft: centerTo }
				}else{
					if(center == 'step' || $(hook).outerHeight() >= winHeight){ 
						var centerTo = step.show().offset().top - (winHeight/2) + (step.outerHeight()/2);//.show() = important!
					}else{
						var centerTo = $(hook).offset().top - (winHeight/2) + ($(hook).outerHeight()/2); 	
					}

					var scrollDir = { scrollTop: centerTo }
				}

				/**
				* Animation callback gets triggerd twice due the compatibilty of the browsers(html,body).
				* This value prevents a double run.
				**/	
				var oro = false; 
			
				/**
				* Animate and highlight.
				**/	
				$('html, body').stop(true, true).animate(scrollDir, 1000, 'swing', function(){
					if(oro){
						if(highlight === true && bd.attr(d_pwid) !== undefined){
							$(hook).addClass(c_pwdc);
							$('#'+c_pwmk).fadeIn(highlightStartSpeed);
						}
					}else{
						oro = true; 
						resetHighlight();//savety reset
					}
				});	
	
			}else{
				if(highlight === true && bd.attr(d_pwid) !== undefined){
					$(hook).addClass(c_pwdc);
					$('#'+c_pwmk).fadeIn(highlightStartSpeed);					
				}
			}
		},
		
		/**
		* RUN TOUR.
		*
		* All code to run the tour.
		**/
		_runTour: function(){ 
		
			var self = this;
							
			/**
			* Get all tour variables.
			**/	
			self._tourDataVars();	

			/**
			* Add run indicator.
			**/	
			if(bd.attr(d_pwrn) == undefined){
				bd.attr(d_pwrn, 'true');				
			}
			
			/**
			* Previouse step.
			**/				
			if(ps !== undefined){
				
				/**
				* Run previous step fx.
				**/	
				self._fxOut(psObj);

			}
			
			setTimeout(function(){	
			
				/**
				* Scroll to target.
				**/	
				self._goTo(hook, csObj, center, position, highlight, scrollHorizontal);
				
				/**
				* Current(next) step.
				**/	
				if(cs !== undefined){
					setTimeout(function(){				

						/**
						* Run current step fx.
						**/	
						self._fxIn(csObj);
						
					},showStepDelay);
				}
				
			},scrollDelay);
		},

		/**
		* END TOUR.
		*
		* All code to end a tour.
		*
		**/
		_endTour: function(){ 

			var self = this;
			
			/**
			* Get all tour variables.
			**/	
			self._tourDataVars();			


			/**
			* Run fxOut.
			**/	
			self._fxOut(csObj);
					
			/**	
			* Remove datasets from body tag and find all open steps and hide these.
			**/
			bd.removeAttr(d_pwid+' '+d_pwcs+' '+d_pwps+' '+d_pwrn);
			
			/**
			* Reset mask.
			**/	
			$('#'+c_pwmk).animate({opacity: 0}, highlightEndSpeed , function(){
				$(this).removeAttr('style');
			});
			
			/**
			* Reset active class.
			**/	
			$('.'+c_pwah).removeClass(c_pwah);
				
			$('.'+c_pwas).removeClass(c_pwas);
			
			/**
			* Reset the zindex issue.
			**/
			setTimeout(function(){	
				if(!bd.attr(d_pwrn)){			
					$('.'+c_pwst).attr('class' , c_pwst+' '+c_pwdc);
				}
			},cssAnimationSpeed);
		},

		/**
		* ACTION BUTTONS.
		**/
		_actionButtons: function(action, goto){ 
		
			var self = this;
			
			/**
			* Get all tour variables.
			**/	
			//self._tourDataVars();	
			
			if(id !== undefined){

				total     = parseInt(countSteps -1);
				cs        = parseInt(cs);
				var allAc = $('[data-powertour-disable-all]');					
					
				/**
				* Choose type of action.
				**/	
				switch(action){
					case 'first':
						if(allAc === undefined && $('[data-powertour-disable-first]').length == 0){
							var newStep = 0;
						}else{
							return false;
						}
					break;
					case 'last':
						if(allAc.length == 0 && $('[data-powertour-disable-last]').length == 0){
							var newStep = total;
						}else{
							return false;
						}		
					break;
					case 'prev':
						if(allAc.length == 0 && $('[data-powertour-disable-prev]').length == 0){
							if(cs <= 0){
								var newStep = 0;	
							}else{
								var newStep = cs - 1;	
							}
						}else{
							return false;
						}
					break;
					case 'next':					
						if(allAc.length == 0 && $('[data-powertour-disable-next]').length == 0){
							if(cs >= total){

								var newStep = total;
								
								if(action != 'next'){
						
									/**
									* End tour.
									**/	
									self._endTour();
								}
														
							}else{
								var newStep = cs + 1;	
							}
						}else{
							return false;
						}
					break;
					case 'stop':
						if(allAc.length == 0 && $('[data-powertour-disable-stop]').length == 0){
							
							/**
							* End tour.
							**/	
							self._endTour();
						
							var newStep = 'stop';
						}else{
							return false;
						}			
					break;
					case 'goto':
						if(allAc.length == 0 && $('[data-powertour-disable-goto]').length == 0){
							var newStep = goto - 1;	
						}else{
							return false;
						}		
					break;
				}

				/**
				* Set new current and previous step values and run the tour.
				**/	
				if(!isNaN(newStep) && newStep != cs){
					bd.attr({
						'data-powertour-currentstep'  : newStep + 1,
						'data-powertour-previousstep' : cs + 1
					});
				
					/**
					* Run the tour.
					**/	
					self._runTour();
				}
			}
		},
		
		/**
		* CLICK EVENTS.
		*
		* All click event like 'start', 'cancel' and 'actions'.
		**/
		_clickEvents: function(){ 
		
			var self = this;

			/**	
			* Start the tour with the trigger.
			**/	
			bd.on(clickEvent, '['+d_pwtg+']', function(e){	
				
				e.stopPropagation();
        		e.preventDefault();
				
        		if(e.handled !== true) {

					/**	
					* End any running tours(savety).
					**/		
					if(bd.attr(d_pwid)){	
						self._endTour();
					}
	
					var id        = $(this).attr(d_pwtg);
					var startWith = $(this).attr(d_pwsw);
					
					if(id !== undefined){
						
						/**
						* Add dataset values to the body.
						**/	
						bd.attr({
							'data-powertour-tour'        : id,
							'data-powertour-currentstep' : startWith
						});
		
						/**
						* Run the tour.
						**/	
						self._runTour();
					}

	
					/**
					* Disable only the anchors.
					**/	
					if($(this).is('a') || $(this).parents().is('a')){
						e.preventDefault();
					}
										
					e.handled = true;
					
				}else{
				
					return false;
				
				}
				
			});

			/**	
			* All action buttons.
			**/	
			bd.on(clickEvent, '['+d_pwac+']:not(.powertour-disable-action)', function(e){
				
				e.stopPropagation();
        		e.preventDefault();
				
        		if(e.handled !== true) {
					
					self._actionButtons($(this).attr(d_pwac), $(this).attr('data-powertour-goto'));
				
					e.preventDefault();
				
					e.handled = true;
					
				}else{
				
					return false;
				}
			});
			
			/**	
			* Easy cancel allows a user(if this option is set to true and
			* a tour is running) to cancel the tour just by clicking somewhere 
			* on the page(excluding the steps them self and any kind of tour buttons).
			**/	
			$(document).on(clickEvent, this, function(e){				

				var id = bd.attr(d_pwid);
				
				if(id !== undefined 
					&&
					self.o.tours[id == 0 ? 0 : id -1].easyCancel !== false
					&&
					!$(e.target).is($('.'+c_pwdc))
					&&
					!$(e.target).is($('.'+c_pwdc+' *'))
					&&
					!$(e.target).is($('.'+c_pwah))
					&&
					!$(e.target).is($('.'+c_pwah+' *'))
					&&
					!$(e.target).is($('['+d_pwac+']'))
					&&
					!$(e.target).is($('['+d_pwac+'] *'))){
				
						e.stopPropagation();
						e.preventDefault();
						
						if(e.handled !== true) {
			
							/**
							* End the tour.
							**/	
							self._endTour(); 
							
							e.handled = true;
							
						}else{
						
							return false;
						
						} 
						 
				}

			});
		},
		
		/**
		* KEYBOARD EVENTS.
		*
		* Manage the tour with the keyboard.
		**/
		_keyboardEvents: function(){ 
		
			var self  = this;
			
			/**	
			* ESC key needs to be keyup. 
			**/
			bd.on('keyup', function(e){
				
				var id = bd.attr(d_pwid);
						
				if(id !== undefined && self.o.tours[id == 0 ? 0 : id -1].escKeyCancel !== false && e.keyCode == 27){	
					
					/**
					* End the tour.
					**/	
					self._endTour();  
				}
			});

			/**	
			* Space key needs to be keydown. 
			**/
			bd.on('keydown', function(e){
				
				var id = bd.attr(d_pwid);
				
				if(id !== undefined && self.o.tours[id == 0 ? 0 : id -1].keyboardNavigation !== false){

					/**	
					* Arrow left key.
					**/								
					if(e.keyCode == 37){
						self._actionButtons('prev', false);
					}
					
					/**	
					* Arrow right key.
					**/
					if(e.keyCode == 39){
						self._actionButtons('next', false);
					}
					
				}
			});
		},
		
		/**
		* RUN ON START.
		*
		* Run on load with the 'powertour' param or the 'rnOnLoad' option.
		**/
		_runOnStart: function(){ 

			var self = this;
			
			/**
			* Get the param values.
			**/	
			function getParam(variable){
				var query = window.location.search.substring(1);
				var vars = query.split("&");
				for(var i=0;i<vars.length;i++) {
					var pair = vars[i].split("=");
					if(pair[0] == variable){return pair[1];}
				}
				return(false);
			}

			var startTour = getParam("powertour");
			var startWith = getParam("startwith");	
				
			/**
			* Check and set the values.
			**/	
			if(!isNaN(startTour) && startTour !== false && startTour !== undefined){

				if(startTour != 0){
					var id = startTour;
				}else{
					var id = 1;
				}
			
				if(!isNaN(startWith) && startWith !== false && startWith !== undefined){
					var cs = startWith;
				}else{
					var cs = 1;
				}

				/**
				* Add dataset values to the body.
				**/	
				bd.attr({
					'data-powertour-tour'        : id,
					'data-powertour-currentstep' : cs
				});
		
				/**
				* Run the tour.
				**/	
				self._runTour();	
			}
		},
		
		/**
		* UPDATE
		*
		* Update the steps postion if this has not been step or has been changed.
		*
		* @param: hook | string/array | The hook thats being re-positioned.
		**/
		update: function(hook){ 
		
			var self = this; 
			
			/**
			* If not array make array.
			**/	
			if(!$.isArray(hook)){
				var hook = [hook];
			}			
	
			/**
			* Update every given step.
			**/	
			$.each(hook, function(i, h){

				var ti  = $(h).parent().attr(d_pwid);
				var si  = $(h).parent().attr(d_pwst);			
			
				var oti = (ti == 0) ? 0 : ti -1;
				var osi = (si == 0) ? 0 : si -1;
				
				var h   = self.o.tours[oti];				
				var s   = h.steps[osi];	

				/**
				* Setting the position.
				**/	
				self._setPosition(s, ti, si, h, false);	
			});
		
		},

		/**
		* NAVIGATION
		*
		* Programmatically access the navigation.
		*
		* @param: nav | string/array | Type of naviagtion is called.
		**/
		navigation: function(nav){ 
		
			var self = this;
			
			if($.isArray(nav)){			
				self._actionButtons(nav[0], nav[1]);
			}else{
				self._actionButtons(nav, false);
			}
		},
		
        /**
		* RUN
		*
		* Run the tour.
		*
		* @param: tour | integer/array | The tour that is called.
		**/
		run: function(tour){ 
		
			var self = this; 

			/**
			* Check if its an array of string.
			**/	
			if($.isArray(tour)){
								
				if(!isNaN(tour[0]) && !isNaN(tour[1])){
	 
					var tourVar   = tour[0];
					var startWith = tour[1];
	
				}else{
					
					var tourVar   = 1;
					var startWith = 1;
					
				}
				
				var startWith = (startWith == 0) ? 1 : startWith;
				
			}else{

				if(!isNaN(tour)){
	 
					var tourVar = tour;
	
				}else{
					
					var tourVar = 1;
					
				}
		
				var st        = self.o.tours[(tourVar == 0) ? 0 : tourVar -1].startWith;				
				var startWith = (st == '' || st == ' ' || st == undefined)? 1 : st;
				
			}
			
			/**
			* Add dataset values to the body.
			**/	
			bd.attr({
				'data-powertour-tour'        : (tourVar == 0) ? 1 : tourVar,
				'data-powertour-currentstep' : startWith
			});

			/**
			* Run the tour.
			**/	
			self._runTour();
					
		},
		
		/**
		* END TOUR(S)
		*
		* Stops all tours.
		**/
		end: function(){ 
		
			var self = this; 
			
			/**
			* End tour.
			**/	
			self._endTour();
			
		},
		
		/**
		* DESTROY TOUR(S)
		*
		* Destroy all tours.
		**/
		destroy: function(){ 
		
			var self = this; 

			if(!isNaN(bd.attr(d_pwid))){

				/**
				* Stop countdown timer.
				**/	
				clearInterval(window.cdInterval);
				
			}

			/**
			* Unwrap every step.
			**/	
			$('.'+c_pwst).children().hide().unwrap();

			/**
			* Remove the mask element.
			**/	
			$('#'+c_pwmk).remove();

			/**
			* Remove all custom dataset attributes from the trigger(s).
			**/	
			$('['+d_pwtg+']').removeAttr(d_pwtg+' '+d_pwsw).removeClass(c_pwdc);
	
			/**
			* Remove custom classes.
			**/						
		    $('.'+c_pwhk).removeClass(c_pwhk);
			
			/**
			* Reset/remove all events.
			**/				
			bd.off('click', self._clickEvents()).off('keydown keyup', self._keyboardEvents()).removeData(pluginName);
		}
		
	};

	$.fn[pluginName] = function(option, param) {
  		return this.each(function() {
			var $this   = $(this);
            var data    = $this.data(pluginName);
            var options = typeof option == 'object' && option;
			if(!data){ 
			  $this.data(pluginName, (data = new Plugin(this, options)))
			}
			if(typeof option == 'string'){
				 data[option](param);
			}
		});
	};

	/**
	* Default settings(dont change).
	* You can globally override these options
	* by using $.fn.pluginName.key = 'value';
	**/
	$.fn[pluginName].defaults = {
		tours:[
				{									
				trigger: '',
				startWith: '',
				easyCancel: '',
				escKeyCancel: '',
				scrollHorizontal: '',                                
				keyboardNavigation: '',
				highlightStartSpeed:'',
				highlightEndSpeed:'',	
				steps:[
				    {
					hookTo: '',
					content: '',
					width: '',
					position: '',
					offsetY: '',
					offsetX: '',
					showStepDelay:'',
					center: '',
					scrollDelay:'',
					highlight: ''
					}		
				]
			}		
		]
	};
	
	// Call directly from jQuery on 'body'
	$[pluginName] = function() {
		var $body = $(document.body);
		$body[pluginName].apply($body, arguments);
	}
			
}));