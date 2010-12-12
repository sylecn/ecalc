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
     | keyboard key |         keyCode | virtualKey in vm |
     |--------------+-----------------+------------------|
     | numbers      |           48-57 | NUM0 to NUM9     |
     | .            |             190 | DOT              |
     | +            | 61 (with shift) | ADD              |
     | -            |             109 | SUBTRACT         |
     | BACKSPACE    |               8 | BACKSPACE        |
     | ENTER        |              13 | EQUAL            |
     | =            |              61 | EQUAL            |
     | c            |              67 | CLEAR            |
     | q            |              81 | RESET            |
     */
    translate: function (event) {
	var keyCode = event.keyCode;
	var shiftKey = event.shiftKey;
	if ((keyCode >= 48) && (keyCode <= 57)) {
	    return 'NUM' + (keyCode - 48);
	}
	switch (keyCode) {
	case 190: return 'DOT';
	case 61:
	    if (shiftKey) {
		return 'ADD';
	    } else {
		return 'EQUAL';
	    }
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
	if (event.keyCode === 67 ||
	    event.keyCode === 81) {
	    // CLEAR or RESET will also clear web page.
	    this.clearLog();
	}

	var key = this.translate(event);
	if (key) {
	    this.defaultVM.pressKey(key);
	} else {
	    if (! utils.inList(event.keyCode,
			     [
				 // modifiers
				 16, 17, 18, 20,
				 // page navigate
				 33, 34, 35, 36,
				 37, 38, 38, 40
			     ])) {
		this.log('key ignored, keyCode = ' + event.keyCode);
	    }
	}
	this.updateUI();
    },
    // keypress: function (event) {
    // 	var key = event.charCode;
    // 	if (this.debugKeyEvent) {
    // 	    this.log('keypress event: charCode = ' + key);
    // 	}
    // },
    /**
     * major dispatcher. edit and recompute history, update UI.
     */
    clickOnHistory: function (event) {
	var td = event.target;
	var oldValue;
	x = td.getAttribute('data-x') || '';
	y = td.getAttribute('data-y') || '';
	type = td.getAttribute('data-type') || '';
	// ecalc.log('clickOnHistory: ' + event.target.tagName +
	// 	  ' x = ' + x +
	// 	  ' y = ' + y +
	// 	  ' type = ' + type);

	// edit this number node
	switch (type) {
	case 'nS':
	    oldValue = this.defaultVM.history._all[x].nS[y];
	    // ecalc.log(oldValue);
	    // make this cell editable.
	    $(td).replaceWith('<input type="text" name="number" id="number" \
size="5px" value="" onkeydown="ecalc.inputNumberKeyDown(event)" />');
	    $('#number').val(oldValue);
	    // a closure for x, y, oldValue.
	    ecalc.confirmEditNumber = function () {
		var newValue = $('#number').val();
		try {
		    newValue = parseFloat(newValue);
		} catch (e) {
		    ecalc.message('Only accept number and dot.');
		    newValue = false;
		}
		if (newValue && (newValue !== oldValue)) {
		    this.defaultVM.history._all[x].nS[y] = newValue;
		    this.defaultVM.history.recompute(x);
		}
		this.updateUI();
		ecalc.confirmEditNumber = utils.noop;
	    };
	    $('#number').focus().select();
	    // update this node, and recompute the session
	    break;
	case 'oS':
	    oldValue = this.defaultVM.history._all[x].oS[y];
	    // ecalc.log(oldValue);
	    // make this cell editable.
	    $(td).replaceWith('<input type="text" name="op" id="op" \
size="5px" value="" onkeydown="ecalc.inputOpKeyDown(event)" />');
	    $('#op').val(this.defaultVM.history.printOp(oldValue));
	    // a closure for x, y, oldValue.
	    ecalc.confirmEditOp = function () {
		var newValue = $('#op').val();

		switch (newValue) {
		case '+':
		    newValue = 'ADD';
		    break;
		case '-':
		    newValue = 'SUBTRACT';
		    break;
		default:
		    // give up, alert the user
		    ecalc.message('Only accept +/-');
		    newValue = false;
		}
		if (newValue && (newValue !== oldValue)) {
		    this.defaultVM.history._all[x].oS[y] = newValue;
		    this.defaultVM.history.recompute(x);
		}
		this.updateUI();
		ecalc.confirmEditOp = utils.noop;
	    };
	    $('#op').focus().select();
	    // update this node, and recompute the session
	    break;
	default:
	    // ignore
	    ecalc.log('ignore click.');
	}
    },
    inputNumberKeyDown: function (event) {
	switch (event.keyCode) {
	case 13:
	    ecalc.confirmEditNumber();
	    break;
	case 27:
	    ecalc.updateUI();
	    break;
	default: //do nothing
	}
    },
    inputOpKeyDown: function (event) {
	switch (event.keyCode) {
	case 13:
	    ecalc.confirmEditOp();
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
    }
};

// main
$(document).ready(function () {
    ecalc.log("ecalc ready.");

    /**
     * init UI
     */
    $('#input-number').hide();

    /**
     * virtual machine
     */
    ecalc.defaultVM = new ecalc.vm.VirtualMachine("defaultVM");
    ecalc.updateUI();

    utils.assertEqual([3, 7, 11, 16],
		      ecalc.defaultVM.history.compute(
			  [1, 2, 4, 4, 5], ['ADD', 'ADD', 'ADD', 'ADD']),
		      'History.compute is broken.');

    ecalc.makeTestInputOneToFive();
    ecalc.makeTestInputOneToFiveMinorDifference();
    ecalc.makeTestInputBusinessData();
    ecalc.makeTestInputBusinessDataMinorDifference();
    ecalc.updateUI();
});
