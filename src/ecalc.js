var ecalc = {
    // debug control variables
    debugKeyEvent: false,
    /**
     * general log function, used for debugging.
     */
    log: function (msg) {
	var old = $('#log').text();
	$('#log').text(old + msg + '\n');
    },
    clearLog: function () {
	$('#log').text('');
    },
    messageList: [],
    /**
     * show a message to end user.
     *
     * message only shown when you call updateUI.
     */
    message: function (msg) {
	// this.log('added user message: ' + msg + '\n');
	this.messageList.push(msg);
    },
    /**
     translate keyCode to virtual key in ecalc.vm
     acceptable keyCode:
     | keyboard key |   keyCode | virtualKey in vm |
     |--------------+-----------+------------------|
     | numbers      |     48-57 | NUM0 to NUM9     |
     | .            |       190 | DOT              |
     | +            | 61 +shift | ADD              |
     | -            |       109 | SUBTRACT         |
     | *            | 56 +shift | MULTIPLY         |
     | BACKSPACE    |         8 | BACKSPACE        |
     | ENTER        |        13 | EQUAL            |
     | =            |        61 | EQUAL            |
     | c            |        67 | CLEAR            |
     | q            |        81 | RESET            |
     */
    translate: function (event) {
	var keyCode = event.keyCode;
	// with modifiers
	if (event.shiftKey) {
	    switch (keyCode) {
	    case 61: return 'ADD';
	    case 56: return 'MULTIPLY';
	    default: //do nothing
	    }
	}
	if (! utils.noModifierKey(event)) {
	    return undefined;
	}
	// no modifiers
	if ((keyCode >= 48) && (keyCode <= 57)) {
	    return 'NUM' + (keyCode - 48);
	}
	switch (keyCode) {
	case 190: return 'DOT';
	case 61: return 'EQUAL';
	case 109: return 'SUBTRACT';
	case 8: return 'BACKSPACE';
	case 13: return 'EQUAL';
	case 67: return 'CLEAR';
	case 81: return 'RESET';
	default: return undefined;
	}
    },
    // keyup: function (event) {
    // 	var key = event.keyCode;
    // 	if (this.debugKeyEvent) {
    // 	    this.log('keyup event: keyCode = ' + key);
    // 	}
    // },
    /**
     * the main dispatcher. listen to keyboard events.
     */
    keydown: function (event) {
	if (this.debugKeyEvent) {
	    this.log('keydown event: keyCode = ' + event.keyCode);
	}

	// skip all input control
	if (utils.isInputNode(event.target)) {
	    return;
	}

	if (event.keyCode === 27) {
	    // ESC clear web page, not passed to VM.
	    this.clearLog();
	    return;
	}
	if ((event.keyCode === 67 || event.keyCode === 81) &&
	    utils.noModifierKey(event)) {
	    // CLEAR or RESET will also clear web page.
	    this.clearLog();
	}

	var key = this.translate(event);
	if (key) {
	    this.defaultVM.pressKey(key);
	    this.updateUI();
	} else {
	    if (utils.noModifierKey(event) &&
		(! utils.inList(event.keyCode,
				[
				    // keyCode for modifiers
				    16, 17, 18, 20,
				    // keyCode for page navigate keys
				    33, 34, 35, 36,
				    37, 38, 38, 40
				]))) {
		this.log('key ignored, keyCode = ' + event.keyCode);
	    }
	}
    },
    // keypress: function (event) {
    // 	var key = event.charCode;
    // 	if (this.debugKeyEvent) {
    // 	    this.log('keypress event: charCode = ' + key);
    // 	}
    // },
    /**
     * delete a history session. update UI.
     */
    deleteHistory: function (event, index) {
	if (utils.isInputNode(event.target)) {
	    return;
	}
	this.log('deleteHistory index = ' + index);
	this.defaultVM.history._all.splice(index, 1);
	this.updateUI();
	return false;
    },
    /**
     * edit Number in history. recompute and update UI if number changed.
     * @param x index for this.defaultVM.history._all
     * @param y index for this.defaultVM.history._all[x].nS
     */
    clickOnNumber: function (event, x, y) {
	var td = event.target;
	var oldValue = this.defaultVM.history._all[x].nS[y];
	// ecalc.log(oldValue);
	var id = 'number-' + x + '-' + y;
	$(td).replaceWith(['<input type="text" name="',
			   id, '" id="', id, '" size="5px" value="',
			   oldValue, '" onkeydown="ecalc.inputNumberKeyDown(\
event, ',
			   x, ',', y, ',', oldValue,')" />'].join(''));
	$('#' + id).focus().select();
    },
    /**
     * edit Op in history. recompute and update UI if number changed.
     * @param x index for this.defaultVM.history._all
     * @param y index for this.defaultVM.history._all[x].oS
     */
    clickOnOp: function (event, x, y) {
	var td = event.target;
	var oldValue = this.defaultVM.history._all[x].oS[y];
	// ecalc.log(oldValue);
	var id = 'number-' + x + '-' + y;
	$(td).replaceWith(['<input type="text" name="',
			   id, '" id="', id, '" size="5px" value="',
			   this.vm.printOp(oldValue),
			   '" onkeydown="ecalc.inputOpKeyDown(\
event, ',
			   // gee. quotes in HTML and javascript.
			   x, ', ', y, ', \'', oldValue,'\')" />'].join(''));
	$('#' + id).focus().select();
    },
    /**
     * confirm or giveup edit a number in history.
     */
    inputNumberKeyDown: function (event, x, y, oldValue) {
	var td, newValue;
	switch (event.keyCode) {
	case 13:
	    // confirm edit number
	    var newValue = $(event.target).val();
	    try {
		newValue = parseFloat(newValue);
	    } catch (e) {
		ecalc.message('Only accept number and dot.');
		newValue = NaN;
	    }
	    if (newValue && (newValue !== oldValue)) {
		this.log('inputNumberKeyDown: ' + oldValue + ' -> ' + newValue);
		this.defaultVM.history._all[x].nS[y] = newValue;
		this.defaultVM.history.recompute(x);
		this.updateUI();
	    }
	    break;
	case 27:
	    ecalc.updateUI();
	    break;
	default: //do nothing
	}
    },
    /**
     * confirm or giveup edit a number in history.
     */
    inputOpKeyDown: function (event, x, y, oldValue) {
	var td, newValue;
	// this.log('inputOpKeyDown: keyCode=' + event.keyCode);
	switch (event.keyCode) {
	case 13:
	    // confirm edit number
	    var newValue = $(event.target).val();
	    // this.log('inputOpKeyDown: newValue=' + newValue);
	    newValue = this.vm.vkeyForOp(newValue);
	    if (newValue === false) {
		// give up, alert the user
		ecalc.message('Only accept +/-');
	    }
	    if (newValue && (newValue !== oldValue)) {
		this.log('inputOpKeyDown: ' + oldValue + ' -> ' + newValue);
		this.defaultVM.history._all[x].oS[y] = newValue;
		this.defaultVM.history.recompute(x);
		this.updateUI();
	    }
	    break;
	case 27:
	    ecalc.updateUI();
	    break;
	default: //do nothing
	}
    },
    hide: function (event) {
	$(event.target.parentNode).hide();
	return false;
    },
    updateUI: function () {
	$('#screen').text(this.defaultVM.screen_asText());
	$('#vms').html(this.defaultVM.asHTML());
	$('#history').html(this.defaultVM.history.asHTML());

	$('#message').text(this.messageList.join('\n'));
	this.messageList = [];
    },
    // functions used in HTML
    /**
     * toggle debug key event.
     */
    toggleDebugKeyEvent: function () {
	this.debugKeyEvent = $('#debug_key_event')[0].checked;
    },
    toggleShowVMStates: function () {
	$('#vms').toggle();
    },
    // test only
    makeTestInputOneToFive: function () {
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM2');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM3');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM4');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM5');
	ecalc.defaultVM.pressKey('EQUAL');
    },

    makeTestInputOneToFiveWithMultiply: function () {
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM2');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM3');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM4');
	ecalc.defaultVM.pressKey('MULTIPLY');
	ecalc.defaultVM.pressKey('NUM5');
	ecalc.defaultVM.pressKey('EQUAL');
    },

    makeTestInputOneToFiveMinorDifference: function () {
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM2');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM4');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM4');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM5');
	ecalc.defaultVM.pressKey('EQUAL');
    },

    makeTestInputBusinessData: function () {
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM3');
	ecalc.defaultVM.pressKey('NUM9');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM5');
	ecalc.defaultVM.pressKey('NUM0');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM4');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM3');
	ecalc.defaultVM.pressKey('NUM8');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM9');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM6');
	ecalc.defaultVM.pressKey('NUM2');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM6');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM9');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM0');
	ecalc.defaultVM.pressKey('BACKSPACE');
	ecalc.defaultVM.pressKey('BACKSPACE');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM9');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM5');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('EQUAL');
    },

    makeTestInputBusinessDataMinorDifference: function () {
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM3');
	ecalc.defaultVM.pressKey('NUM9');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM5');
	ecalc.defaultVM.pressKey('NUM0');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM4');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM3');
	ecalc.defaultVM.pressKey('NUM8');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM9');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM6');
	ecalc.defaultVM.pressKey('NUM2');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM6');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM0');
	ecalc.defaultVM.pressKey('BACKSPACE');
	ecalc.defaultVM.pressKey('BACKSPACE');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM9');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('NUM5');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('DOT');
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('EQUAL');
    },

    makeTestInputForContinuesCalc: function () {
	ecalc.defaultVM.pressKey('NUM1');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM2');
	ecalc.defaultVM.pressKey('EQUAL');
	ecalc.defaultVM.pressKey('ADD');
	ecalc.defaultVM.pressKey('NUM3');
	ecalc.defaultVM.pressKey('EQUAL');
    }
};

// main
$(document).ready(function () {
    ecalc.log("ecalc ready.");

    /**
     * init UI
     */
    if ($('#show_vm_states')[0].checked) {
        $('#vms').show();
    } else {
        $('#vms').hide();
    }

    /**
     * virtual machine
     */
    ecalc.defaultVM = new ecalc.vm.VirtualMachine("defaultVM");
    ecalc.updateUI();

    // ============
    //  unit tests
    // ============

    utils.assertEqual([3, 7, 11, 16],
		      ecalc.defaultVM.history.compute(
			  [1, 2, 4, 4, 5], ['ADD', 'ADD', 'ADD', 'ADD']),
		      'History.compute is broken.');
    utils.assertEqual([3, 12, 8, 13],
		      ecalc.defaultVM.history.compute(
			  [1, 2, 4, 4, 5],
			  ['ADD', 'MULTIPLY', 'SUBTRACT', 'ADD']),
		      'History.compute is broken.');
    ecalc.testData = {};
    ecalc.testData.a = [0, 1, 2, 3, 4];
    ecalc.testData.a.splice(2, 1);
    utils.assertEqual([0, 1, 3, 4], ecalc.testData.a,
		      'Array.splice failed.');



    // ==========
    //  UI tests
    // ==========
    if (true) {
	ecalc.makeTestInputForContinuesCalc();

	utils.assertEqual(6, ecalc.defaultVM.getResult(),
			 'wrong result for makeTestInputForContinuesCalc');
    }

    if (true) {
	ecalc.makeTestInputOneToFiveWithMultiply();
	utils.assertEqual(50, ecalc.defaultVM.getResult(),
			  'wrong result for makeTestInputOneToFiveWithMultiply'
			 );
	// ecalc.makeTestInputOneToFive();
	// ecalc.makeTestInputOneToFiveMinorDifference();
	ecalc.makeTestInputBusinessData();
	ecalc.makeTestInputBusinessDataMinorDifference();
    }
    ecalc.updateUI();
});
