# -*- coding: UTF-8 -*-

from main_process import *
from sanic import Sanic
from sanic import response
#配置信息

config_dict = json.load(open("config/core_config.json", 'r'))
nlu_config = config_dict["nlu_config"]

app = Sanic()
Responnd = Responds()

@app.route("/health", methods=['GET'])
async def health(request):
    result = {'status': 'UP'}
    return response.json(result)

@app.route('/', methods=['GET'])
async def get_name(request):
    logger.info(request)
    return response.json({"name": "sanic_service"})


@app.route('/state', methods=['GET'])
async def get_state(request):
    return response.json(Responnd.__dict__)

@app.route('/test', methods=['POST'])
async def post_nlu(request):
    request_json = request.body
    logger.info(type(request_json))
    input_json = json.loads(request_json.decode('utf8'))
    intent = nlu_intent(input_json["q"], ip, nlu_config)
    return response.json(intent)
#定义
@app.route('/responds/', methods=['POST'])
async def post_handler(request):
    request_json = request.body
    input_json = json.loads(request_json.decode('utf8'))
    logger.info(input_json)
    sender_id = input_json['sender_id']
    query = input_json['query']
    domain_intent = input_json["domain_intent"]
    callState = input_json["callState"]
    logger.info(Responnd.__dict__)
    '''
    if query != "null":
        intent = nlu_intent(query, ip, nlu_config)
        if intent:
            domain_intent = {intent : list(dict_intent_domain[intent])}
            logger.info("******{}".format(domain_intent))
    '''
    try:
        if 'phoneAccount' in input_json.keys():
            person = input_json['phoneAccount'][0]
            current_action, answer, action_synthesis, action_num, entities, current_statue, timeout_nums, node_label, error_msg = Responnd.solve_intents(sender_id, query, domain_intent, callState, person)
        else:
            current_action, answer, action_synthesis, action_num, entities, current_statue, timeout_nums, node_label, error_msg = Responnd.solve_intents(sender_id, query, domain_intent, callState)
    except Exception: 
        logger.info('traceback error', exc_info=True)  
    logger.info(Responnd.__dict__)
    res_js = {"answer1": answer, "answer2": action_synthesis, "answer3": action_num, "entities": entities, "currentAction": current_action, "dialogState": current_statue, "timeoutNums": timeout_nums, "nodeLabel": node_label, "failMsg": error_msg}
    logger.info(res_js)
    print(res_js)
    return response.json(res_js)
    
if __name__ == "__main__":
    ip = config_dict["ip"]
    port = config_dict["sanic_server"][0]
    app.run(host=ip, port=port)