/*
 * the virtual machine can work on its own without ecalc UI. But it remains in
 * ecalc.vm namespace.
 */
if (typeof ecalc === 'undefined') {
    var ecalc = {};
    alert('Warning: ecalc not defined.');
}

ecalc.vm = {
    log: function (msg) {
	ecalc.log(msg);
    },
    /**
     * constructor for a VM.
     */
    VirtualMachine: function (name) {
	var _virtualKeys = [
	    // ================
	    //  basic features
	    // ================
	    // digits
	    'NUM0', 'NUM1', 'NUM2', 'NUM3', 'NUM4',
	    'NUM5', 'NUM6', 'NUM7', 'NUM8', 'NUM9',
	    'DOT', 'NUM00',
	    // op
	    'ADD', 'SUBTRACT',
	    // misc
	    'BACKSPACE', 'EQUAL', 'CLEAR'
	    // ===================
	    //  advanced features
	    // ===================
	];

	/**
	 * screen obejct
	 */
	// Don't know how to know the outter 'this' in inner object.
	// the following doesn't work.
	// this.screen = function () {
	//     var that = this;

	//     return {
	// 	asText: function () {
	// 	    return that._partialNumber || '0';
	// 	}
	//     };
	// } ();

	this.screen_asText = function () {
	    if (this._calcDone || this._partialCalcDone) {
		return this._numberStack.last();
	    } else {
		return this._partialNumber || '0';
	    }
	};

	/**
	 * reset partial number states
	 */
	this._resetPartialNumber = function () {
	    this._acceptDot = true;
	    this._partialNumber = '';
	}

	/**
	 * accept partial number.
	 */
	this._acceptPartialNumber = function () {
	    this._numberStack.push(parseFloat(this._partialNumber) || 0);
	    this._resetPartialNumber();
	};

	/**
	 * CLEAR key pressed
	 */
	this._clear = function () {
	    // key stack
	    this._keyStack = [];
	    // stacks
	    this._numberStack = [];
	    this._opStack = [];

	    this._resetPartialNumber();
	    this._calcDone = false;
	    this._partialCalcDone = false;
	};

	/**
	 * if opStack is not empty. do calculation.
	 */
	this._doCalculationMaybe = function () {
	    var value1, value2;

	    op = this._opStack.pop();
	    if (op) {
		// calculate for last OP in stack
		value2 = this._numberStack.pop();
		value1 = this._numberStack.pop();
		switch (op) {
		case 'ADD':
		    this._numberStack.push(value1 + value2);
		    break;
		case 'SUBTRACT':
		    this._numberStack.push(value1 - value2);
		    break;
		default:
		    utils.assert(false, '_doCalculationMaybe(): bad op: ' + op);
		}
		this._partialCalcDone = true;
	    }
	};

	// trivial
	this.name = name;
	this._keyStack = [];

	// stacks
	this._numberStack = [];
	this._opStack = [];

	// state control
	this._calcDone = false;
	this._acceptDot = true;
	this._partialNumber = '';

	// represent this VM states in HTML
	this.asHTML = function () {
	    return '<div class="vm">Virtual Machine: ' + name +
		'<br/>keyStack: ' + this._keyStack.join(' ') +
		'<br/>_numberStack: ' + this._numberStack.join(' ') +
		'<br/>_opStack: ' + this._opStack.join(' ') +
		'<br/>_partialNumber: ' + this._partialNumber +
		'<br/>_calcDone: ' + this._calcDone +
		'</div>';
	}
	/**
	 * press key on virtual machine.
	 */
	this.pressKey = function (key) {
	    var value1, value2, op;
	    utils.assertTrue(utils.inList(key, _virtualKeys),
			    'pressKey(): bad key: ' + key);
	    this._keyStack.push(key);

	    switch (key) {
	    case 'CLEAR':
		this._clear();
		break;
	    case 'NUM0':  //all all the way through
	    case 'NUM00':
	    case 'NUM1':
	    case 'NUM2':
	    case 'NUM3':
	    case 'NUM4':
	    case 'NUM5':
	    case 'NUM6':
	    case 'NUM7':
	    case 'NUM8':
	    case 'NUM9':
		if (key == 'NUM0' || key == 'NUM00') {
		    if (this._partialNumber) {
			this._partialNumber += key.slice(3);
		    } else {
			ecalc.vm.log('ignore none significant ' + key);
		    }
		} else {
		    this._partialNumber += key.slice(3);
		}

		this._partialCalcDone = false;
		if (this._calcDone) {
		    // throw away non-reachable data in stack
		    this._numberStack = [];
		    this._opStack = [];
		    this._calcDone = false;
		}
		break;
	    case 'DOT':
		if (this._acceptDot) {
		    this._partialNumber += '.';
		    this._acceptDot = false;
		} else {
		    ecalc.vm.log('Warning: ignore nonsense DOT.');
		}
		break;
	    case 'EQUAL':
		if (! this._calcDone) {
		    this._acceptPartialNumber();
		    this._doCalculationMaybe();
		    this._calcDone = true;
		} else {
		    ecalc.vm.log('Warning: ignore nonsense EQUAL.');
		}
		break;
	    case 'ADD':
	    case 'SUBTRACT':
		if (this._calcDone) {
		    this._calcDone = false;
		} else {
		    this._acceptPartialNumber();
		}
		this._doCalculationMaybe();

		this._opStack.push(key);

		break;
	    default:
		// never reacher
		utils.assert(false,
			    'function for Key [' + key + '] Not implemented.');
	    }
	}
    }
};
